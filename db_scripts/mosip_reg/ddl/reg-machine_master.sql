-- -------------------------------------------------------------------------------------------------
-- Database Name: mosip_reg
-- Table Name 	: reg.machine_master
-- Purpose    	: 
--           
-- Create By   	: Nasir Khan / Sadanandegowda
-- Created Date	: 15-Jul-2019
-- 
-- Modified Date        Modified By         Comments / Remarks
-- ------------------------------------------------------------------------------------------
-- 07-Apr-2020          Sadanandegowda      Column constraints and data type change
-- Jan-2021		Ram Bhatt	    Set is_deleted flag to not null and default false
-- ------------------------------------------------------------------------------------------

-- NOTE: the code below contains the SQL for the selected object
-- as well for its dependencies and children (if applicable).
-- 
-- This feature is only a convinience in order to permit you to test
-- the whole object's SQL definition at once.
-- 
-- When exporting or generating the SQL for the whole database model
-- all objects will be placed at their original positions.


-- object: reg.machine_master | type: TABLE --
-- DROP TABLE IF EXISTS reg.machine_master CASCADE;
CREATE TABLE reg.machine_master(
	id character varying(10) NOT NULL,
	name character varying(64) NOT NULL,
	mac_address character varying(64),
	serial_num character varying(64),
	ip_address character varying(17),
	public_key  character varying(1024),
	key_index character varying(128),
	sign_public_key  character varying(1024),
	sign_key_index character varying(128),
	validity_end_dtimes timestamp,
	mspec_id character varying(36) NOT NULL,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean NOT NULL DEFAULT FALSE,
	del_dtimes timestamp,
	CONSTRAINT pk_machm_id PRIMARY KEY (id,lang_code)

);
-- ddl-end --
