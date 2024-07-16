package com.pampers.GenericPampersService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "_tbl_obdchannel_master")
public class ObdChannel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="obdchannel_id",unique = true)
	private Long obdChannelId;
	@Column(name="obdchannel_name")
	private String obdchannelName;
	@Column(name="obdchannel_status")
	private String obdchannelStatus;
	@Column(name="obdchannel_context")
	private String obdChannelContext;
	@Column(name="status")
	private int status;

	public Long getObdChannelId() {
		return obdChannelId;
	}

	public void setObdChannelId(Long obdChannelId) {
		this.obdChannelId = obdChannelId;
	}

	public String getObdchannelName() {
		return obdchannelName;
	}

	public void setObdchannelName(String obdchannelName) {
		this.obdchannelName = obdchannelName;
	}

	public String getObdchannelStatus() {
		return obdchannelStatus;
	}

	public void setObdchannelStatus(String obdchannelStatus) {
		this.obdchannelStatus = obdchannelStatus;
	}

	public String getObdChannelContext() {
		return obdChannelContext;
	}

	public void setObdChannelContext(String obdChannelContext) {
		this.obdChannelContext = obdChannelContext;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
