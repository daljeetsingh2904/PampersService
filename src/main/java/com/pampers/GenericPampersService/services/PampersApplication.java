package com.pampers.GenericPampersService.services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.pampers.GenericPampersService.dao.DbHandler;
import com.pampers.GenericPampersService.model.OutboundCallParameters;
import com.pampers.GenericPampersService.model.ServicConfigEntity;
import com.pampers.GenericPampersService.repository.ServiceConfiguration;

@Component
public class PampersApplication {

	private static final Logger logger = Logger.getLogger(PampersApplication.class);

	@Autowired
	private ServiceConfiguration serviceRepo;

	public void callInitiator(ServicConfigEntity serviceparam, DbHandler dbhandler) {
		System.out.println("helo");
		List<OutboundCallParameters> recordsArr;
		String amiCallingResponse = null;
		String outboundParameters;
		while (true) {
			try {

				recordsArr = DbHandler.fetchOutboundNumbers(serviceparam, dbhandler);
				if (recordsArr.isEmpty()) {
					logger.debug("No Numbers are Available for Calling for " + serviceparam.getServiceName()
							+ " so Waiting for " + serviceparam.getObdThreadSleepTime() + " milliseconds");
					Thread.sleep(serviceparam.getObdThreadSleepTime());
				} else {
					for (OutboundCallParameters data : recordsArr) {
						outboundParameters = replaceOutboundParameters(serviceparam, data);
						logger.debug("Initiated Call for Service " + serviceparam.getServiceName()
								+ " for MissCallId-->" + data.getMisscallId() + "-->" + outboundParameters);
						amiCallingResponse = OutboundSender.getInstance(serviceparam).sendCall(serviceparam,
								data.getObdChannelName(), data.getMobileNumber(), outboundParameters);
						callResponse(serviceparam, dbhandler, data,amiCallingResponse);
						logger.info("Submitted Call Response for Service  " + serviceparam.getServiceName()
								+ " for MissCallId-->" + data.getMisscallId() + "-->" + outboundParameters + "-->"
								+ amiCallingResponse);

					}
					OutboundSender.getInstance(serviceparam).openCloseAmiConnection(0);
				}

			} catch (Exception exception) {
				logger.error(exception + Arrays.asList(exception.getStackTrace()).stream().map(Objects::toString)
						.collect(Collectors.joining("\n")));
			}
		}
	}

	private String replaceOutboundParameters(ServicConfigEntity serviceparam, OutboundCallParameters data) {
		return serviceparam.getObdCallParameters().replace("%MISSCALLID", Integer.toString(data.getMisscallId()))
				.replace("%MOBILEID", Integer.toString(data.getMobileNumberId()))
				.replace("%PROJECTID", Integer.toString(data.getProjectId()))
				.replace("%RETRYCOUNT", Integer.toString(data.getRetryCount()))
				.replace("%SITEID", Integer.toString(data.getSiteId())).replace("%LANGUAGENAME", data.getLanguageName())
				.replace("%OBDCHANNELID", Integer.toString(data.getObdChannelId())).replace("%CALLSENDTIME",
						Utility.getCurrentDatetime(serviceparam).toString().replace("T", " ").split("\\.")[0]);
	}

	private void callResponse(ServicConfigEntity serviceparam, DbHandler dbhandler, OutboundCallParameters data,String amiCallingResponse) {
		if (amiCallingResponse.startsWith("Success")) {
			logger.info("Final Status Update Request Initiated for Service " + serviceparam.getServiceName()
					+ " for MissCallId-->" + data.getMisscallId() + "--> and obdChannelID-->" + data.getObdChannelId());
			dbhandler.updateCallStatus((long) data.getMobileNumberId(), amiCallingResponse, Constants.STATUS_FINAL,
					(long) data.getMisscallId());
			dbhandler.updateChannelStatus(Constants.CHANNELBUSYSTATUS, (long) data.getMisscallId(),
					(long) data.getObdChannelId());

		} else {
			logger.info("Error Status Update Request Initiated Service " + serviceparam.getServiceName()
					+ " for MissCallId-->" + data.getMisscallId());
			dbhandler.updateCallStatus((long) data.getMobileNumberId(), amiCallingResponse, Constants.STATUS_ERROR,(long) data.getMisscallId());
//			dbhandler.updateCallStatus(serviceparam, data.getMobileNumberId(), data.getMisscallId(),
//					Constants.STATUS_ERROR, amiCallingResponse);
		}
	}
}