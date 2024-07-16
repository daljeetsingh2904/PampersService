package com.pampers.GenericPampersService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pampers.GenericPampersService.model.ObdChannel;

public interface ObdChannelRepo extends JpaRepository<ObdChannel, Long>{

	  @Query(value="select obdchannel_id,obdchannel_name from _tbl_obdchannel_master where obdchannel_status=:obdChannelStatus and obdchannel_context=:obdChannelContext and status=1" ,nativeQuery = true)
		List<ObdChannel> findIdleChannels(@Param("obdchannel_status") String obdChannelStatus,@Param("obdchannel_context") String obdChannelContext);	
	   
}
