package com.pampers.GenericPampersService.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.pampers.GenericPampersService.model.ObdChannel;
import com.pampers.GenericPampersService.model.OutboundCallParameters;
import com.pampers.GenericPampersService.model.ServicConfigEntity;
import com.pampers.GenericPampersService.repository.MissCallRepo;
import com.pampers.GenericPampersService.repository.ObdChannelRepo;
import com.pampers.GenericPampersService.repository.ServiceConfiguration;
import com.pampers.GenericPampersService.services.Constants;
import com.pampers.GenericPampersService.services.Utility;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class DbHandler {

	@Autowired
	private ServiceConfiguration serviceRepo;

	@Autowired
	private MissCallRepo missCallRepo;
	
	@Autowired
	private ObdChannelRepo obdChannelRepo;

	public DbHandler(ServiceConfiguration serviceRepo) {
		this.serviceRepo = serviceRepo;
	}

	public ServicConfigEntity fetchConfiguration(ServicConfigEntity serviceparam) {
		Optional<ServicConfigEntity> configOpt = serviceRepo.findByServiceName(serviceparam.getServiceName());

		if (configOpt.isPresent()) {
			ServicConfigEntity config = configOpt.get();
			serviceparam.setAmiUsername(config.getAmiUsername());
			serviceparam.setAmiPassword(config.getAmiPassword());
			serviceparam.setAmiHostname(config.getAmiHostname());
			serviceparam.setCallStatus(config.getCallStatus());
			serviceparam.setObdRingTimeout(config.getObdRingTimeout());
			serviceparam.setContextName(config.getContextName());
			serviceparam.setCallerId(config.getCallerId());
			serviceparam.setObdThreadSleepTime(config.getObdThreadSleepTime());
			serviceparam.setObdCallParameters(config.getObdCallParameters());
			serviceparam.setSalt(config.getSalt());
			serviceparam.setSecretKey(config.getSecretKey());
		} else {
			System.out.println("Configuration not found for service: " + serviceparam.getServiceName());
		}

		return serviceparam;
	}

	@Transactional
	public int updateCallStatus(Long mobileNumberId, String response, int status, Long misscallId) {
		int callStatus = missCallRepo.updateCallStatus(mobileNumberId, response, status, misscallId);
		return callStatus;
	}

	@Transactional
	public int updateChannelStatus(String obdChannelStatus, Long missCallId, Long obdChannelId) {
		int channelStatus = missCallRepo.updateChannelStatus(obdChannelStatus, missCallId, obdChannelId);
		return channelStatus;
	}
	

	public List<OutboundCallParameters> fetchOutboundNumbers(ServicConfigEntity serviceparam,
			DbHandler dbhandler) {
//			PropertyConfigurator.configure(serviceparam.getLog4jFilePath());
		String columnName;
		String tableName;
		String columnValues = null;
		String mobileNumber;
		int recordCounter = 0;
		int validateOptionFlag;
		int callCounter = 0;
		ResultSet resultSet = null;
		OutboundCallParameters callingData;
		Map<Integer, String> parameterValues = new HashMap<>();
		List<String> obdChannelData = new ArrayList<>();
		List<String> uniqueIdData = new ArrayList<>();
		List<OutboundCallParameters> obdCallingData = new ArrayList<>();
		StringBuilder dataBuilder = new StringBuilder();
		try {

			obdChannelData = fetchCallingChannels(serviceparam);
			if (obdChannelData.isEmpty()) {
				System.out.println("All Channels for Outbound is Busy or No Channel is Configured for OBD");
			} else {
				resultSet = dbhandler.fetchCallingNumbers(serviceparam, obdChannelData.size());
				while (resultSet.next()) {
					callingData = new OutboundCallParameters();
					callingData.setMisscallId(resultSet.getInt("misscall_id"));
					callingData.setMobileNumber(resultSet.getString(Constants.MOBILENUMBER));
					callingData.setRetryCount(resultSet.getInt("retry_count"));
					if (callingData.getRetryCount() > 0) {
						mobileNumber = dbhandler.fetchMobileNumber(serviceparam, callingData.getMobileNumber());
						if (mobileNumber == null) {
							mobileNumber = callingData.getMobileNumber();
							callingData.setMobileNumberEncrypted(
									Utility.encrypt(serviceparam, callingData.getMobileNumber()));
						} else if (mobileNumber.length() == 24 && mobileNumber.indexOf("==") != -1) {
							callingData.setMobileNumberEncrypted(mobileNumber);
							mobileNumber = Utility.decrypt(serviceparam, mobileNumber);
						}
						callingData.setMobileNumber(mobileNumber);
					} else {
						callingData
								.setMobileNumberEncrypted(Utility.encrypt(serviceparam, callingData.getMobileNumber()));
					}
					uniqueIdData = fetchMobileNumberUniqueId(serviceparam, callingData, dbhandler);
					if (uniqueIdData.get(0).equals("0")) {
						logger.error("Some Error in Generation/Insertion of Mobile Number UniqueId for MissCallId-->"
								+ callingData.getMisscallId());
					} else {
						callingData.setMobileNumberId(Integer.parseInt(uniqueIdData.get(0).split("@")[0]));
						callCounter = dbhandler.getMissCallCount(serviceparam, callingData);
						if (callCounter > 0) {
							dbhandler.updateCallStatus(serviceparam, callingData.getMobileNumberId(),
									callingData.getMisscallId(), Constants.STATUS_ALREADYONCALL, "Already On Call");
						} else {
							if (serviceparam.getCallStatus() == 0) {
								serviceparam.setRegretStatus(Constants.STATUS_REGRET);
								validateOptionFlag = dbhandler.validateOptin(serviceparam, callingData);
							} else if (serviceparam.getCallStatus() == 1) {
								validateOptionFlag = 0;
							} else {
								serviceparam.setRegretStatus(Constants.STATUS_PHARMACY_REGRET);
								validateOptionFlag = dbhandler.validatePharmacyRegistration(serviceparam, callingData);
							}
							switch (validateOptionFlag) {
							case 0:
								dataBuilder.append(callingData.getMisscallId() + ",");
								callingData.setProjectId(resultSet.getInt("project_id"));
								callingData.setSiteId(resultSet.getInt("site_id"));
								callingData.setLanguageName(LanguageRepository.getInstance(serviceparam)
										.getValue(resultSet.getInt(Constants.LANGUAGEID)));
								callingData.setObdChannelId(
										Integer.parseInt(obdChannelData.get(recordCounter).split("@")[0]));
								callingData.setObdChannelName(obdChannelData.get(recordCounter++).split("@")[1]);
								obdCallingData.add(callingData);
								dbhandler.deleteTrialDetails(serviceparam, callingData);
								/*
								 * datacounter=dbhandler.getCount(serviceparam,callingData,1); if(datacounter<1)
								 * { columnName=Constants.SMSCOLUMN; tableName=Constants.SMSENDTABLENAME;
								 * columnValues=Constants.THREECONDITIONVARIABLE; parameterValues.put(1,
								 * Integer.toString(callingData.getMobileNumberId())); parameterValues.put(2,
								 * Integer.toString(1)); parameterValues.put(3,
								 * Integer.toString(callingData.getMisscallId()));
								 * DbConnection.getInstance(serviceparam).insertRecords(columnName, tableName,
								 * columnValues, parameterValues); }
								 */
								break;
							case 1:
								// datacounter=dbhandler.getCount(serviceparam,callingData,4);
								// if(datacounter<1) {
								columnName = Constants.SMSCOLUMN;
								tableName = Constants.SMSENDTABLENAME;
								columnValues = Constants.THREECONDITIONVARIABLE;
								parameterValues.put(1, Integer.toString(callingData.getMobileNumberId()));
								parameterValues.put(2, Integer.toString(serviceparam.getRegretStatus()));
								parameterValues.put(3, Integer.toString(callingData.getMisscallId()));
								DbConnection.getInstance(serviceparam).insertRecords(columnName, tableName,
										columnValues, parameterValues);
								// }
								break;
							default:
								logger.error("Error in Validating Optin Details for MissCallId-->"
										+ callingData.getMisscallId());
								break;
							}
						}
					}
				}
				if (dataBuilder.length() > 0) {
					logger.info("Initial Status Update Request Initiated for MissCallId-->" + dataBuilder.toString());
					dbhandler.updateCallStatus(serviceparam,
							dataBuilder.deleteCharAt(dataBuilder.lastIndexOf(",")).toString(),
							Constants.STATUS_INITIAL);
				}
			}
		} catch (Exception exception) {
			obdCallingData.clear();
			logger.error(exception + Arrays.asList(exception.getStackTrace()).stream().map(Objects::toString)
					.collect(Collectors.joining("\n")));
		} finally {
			parameterValues.clear();
			obdChannelData.clear();
			uniqueIdData.clear();
			dataBuilder.delete(0, dataBuilder.length());
			try {
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (Exception finallyexception) {
				logger.error(finallyexception + Arrays.asList(finallyexception.getStackTrace()).stream()
						.map(Objects::toString).collect(Collectors.joining("\n")));
			}
		}
		logger.debug("Total Number of Records Received for Initiating OBD Call are -->" + obdCallingData.size());
		return obdCallingData;
	}
	
	
	public List<String> fetchCallingChannels(ServicConfigEntity serviceparam) {
		List<String> channelArr=new ArrayList<>();
		List<ObdChannel> idleChannel=new ArrayList<>();
		try {
			idleChannel=obdChannelRepo.findIdleChannels(Constants.CHANNELFREESTATUS, serviceparam.getContextName());
			idleChannel.forEach(channel->channelArr.add(channel.getObdChannelId()+"@"+channel.getObdchannelName()));
			System.out.println("Available Channels are -->"+channelArr);
		}catch(Exception exception) {
            System.out.println("error in fetch calling channel --> "+exception);
		}
		return channelArr;		
	}
}
