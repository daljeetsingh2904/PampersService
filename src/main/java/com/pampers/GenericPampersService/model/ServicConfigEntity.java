package com.pampers.GenericPampersService.model;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

 
import javax.persistence.Id;


@Entity
@Table(name = "_tbl_serviceconfiguration_details")
public class ServicConfigEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="configure_id")
	private Long id;
	@Column(name = "ContextName")
	private String contextName;
	@Column(name = "EncryptionSecretKey")
	private String secretKey;
	@Column(name = "EncryptionSalt")
	private String salt;
	@Column(name = "AmiUserName")
	private String amiUsername;
	@Column(name = "AmiPassword")
	private String amiPassword;
	@Column(name = "AmiHostname")
	private String amiHostname;
	@Column(name = "CallerId")
	private String callerId;
	@Column(name = "ObdCallParameters")
	private String obdCallParameters;
	@Column(name = "ObdRingTimeout")
	private long obdRingTimeout;
	@Column(name = "ObdThreadSleepTime")
	private int obdThreadSleepTime;
	@Column(name = "CallStatus")
	private int callStatus;
	@Column(name="ServiceName")
	private String serviceName;
	private int regretStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getAmiUsername() {
		return amiUsername;
	}

	public void setAmiUsername(String amiUsername) {
		this.amiUsername = amiUsername;
	}

	public String getAmiPassword() {
		return amiPassword;
	}

	public void setAmiPassword(String amiPassword) {
		this.amiPassword = amiPassword;
	}

	public String getAmiHostname() {
		return amiHostname;
	}

	public void setAmiHostname(String amiHostname) {
		this.amiHostname = amiHostname;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getObdCallParameters() {
		return obdCallParameters;
	}

	public void setObdCallParameters(String obdCallParameters) {
		this.obdCallParameters = obdCallParameters;
	}

	public long getObdRingTimeout() {
		return obdRingTimeout;
	}

	public void setObdRingTimeout(long obdRingTimeout) {
		this.obdRingTimeout = obdRingTimeout;
	}

	public int getObdThreadSleepTime() {
		return obdThreadSleepTime;
	}

	public void setObdThreadSleepTime(int obdThreadSleepTime) {
		this.obdThreadSleepTime = obdThreadSleepTime;
	}

	public int getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(int callStatus) {
		this.callStatus = callStatus;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getRegretStatus() {
		return regretStatus;
	}

	public void setRegretStatus(int regretStatus) {
		this.regretStatus = regretStatus;
	}

}
