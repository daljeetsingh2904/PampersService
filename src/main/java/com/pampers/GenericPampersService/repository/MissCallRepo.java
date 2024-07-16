package com.pampers.GenericPampersService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pampers.GenericPampersService.model.MissCallEntity;
import com.pampers.GenericPampersService.model.ServicConfigEntity;

public interface MissCallRepo extends JpaRepository<MissCallEntity, Long> {

	@Modifying
	@Query(value = "Update _tbl_misscall_details set mobile_number=:mobile_number,ami_response=:amiResponse,status=:status,updateDate=now() where misscall_id=:misscallId", nativeQuery = true)
	int updateCallStatus(@Param("mobile_number") Long mobileNumber, @Param("ami_response") String amiResponse,
			@Param("status") int status, @Param("misscall_id") Long misscallId);

	@Modifying
	@Query(value = "Update _tbl_obdchannel_master set obdchannel_status=:obdChannelStatus,misscall_id=:misscall_id,updateDate=now() where obdchannel_id=? and status=1", nativeQuery = true)
	int updateChannelStatus(@Param("obdchannel_status") String obdChannelStatus, @Param("misscall_id") Long misscall_id,
			@Param("obdchannel_id") Long obdChannelId);

     @Query(value="select md.misscall_id,md.mobile_number,md.project_id,md.retry_count,pmm.site_id,sm.language_id from _tbl_misscall_details"
     		+ " md inner join _tbl_projectsite_mapping_master pmm on md.project_id=pmm.project_id inner join _tbl_site_master sm on pmm.site_id=sm.site_id"
     		+ " md.status=:status and if(md.updateDate is NULL,md.createDate<now(),md.updateDate<now()) limit :idleChannel",nativeQuery = true)
     Optional<ServicConfigEntity> fetchCallingNumbers(@Param("status")int status,@Param("idleChannel")int idleChannel);	

}
