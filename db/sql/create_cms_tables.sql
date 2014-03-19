--
-- Table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `first_name` varchar(16) NOT NULL,
  `last_name` varchar(16) NOT NULL,
  `middle_name` varchar(16) DEFAULT NULL,
  `email` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_u1` (`username`),
  UNIQUE KEY `user_u2` (`first_name`, `last_name`, `middle_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `group`
--

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `group_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_group_u1` (`user_id`, `group_id`),
  KEY `user_group_k1` (`user_id`),
  CONSTRAINT `user_group_fk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `user_group_k2` (`group_id`),
  CONSTRAINT `user_group_group_fk2` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `created_on_date_time` datetime NOT NULL,
  `created_by_user_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `log_k1` (`created_by_user_id`),
  CONSTRAINT `log_fk1` FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `resource_category`
--

DROP TABLE IF EXISTS `resource_category`;
CREATE TABLE `resource_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_category_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `resource_type`
--

CREATE TABLE `resource_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `resource_category_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_type_u1` (`name`),
  KEY `resource_type_k1` (`resource_category_id`),
  CONSTRAINT `resource_type_fk1` FOREIGN KEY (`resource_category_id`) REFERENCES `resource_category` (`id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_category`
--

DROP TABLE IF EXISTS `component_category`;
CREATE TABLE `component_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_category_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_type`
--

CREATE TABLE `component_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `component_category_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_type_u1` (`name`),
  KEY `component_type_k1` (`component_category_id`),
  CONSTRAINT `component_type_fk1` FOREIGN KEY (`component_category_id`) REFERENCES `component_category` (`id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `property_category`
--

DROP TABLE IF EXISTS `property_category`;
CREATE TABLE `property_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_category_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `property_type`
--

CREATE TABLE `property_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `property_category_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_type_u1` (`name`),
  KEY `property_type_k1` (`property_category_id`),
  CONSTRAINT `property_type_fk1` FOREIGN KEY (`property_category_id`) REFERENCES `property_category` (`id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `connector_category`
--

DROP TABLE IF EXISTS `connector_category`;
CREATE TABLE `connector_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `connector_category_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `connector_type`
--

CREATE TABLE `connector_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `connector_category_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `connector_type_u1` (`name`),
  KEY `connector_type_k1` (`connector_category_id`),
  CONSTRAINT `connector_type_fk1` FOREIGN KEY (`connector_category_id`) REFERENCES `connector_category` (`id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `connector_type_property`
--

CREATE TABLE `connector_type_property` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `connector_type_id` int(11) unsigned NOT NULL,
  `property_type_id` int(11) unsigned NOT NULL,
  `value` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `connector_type_property_u1` (`connector_type_id`, `property_type_id`, `value`),
  KEY `connector_type_property_k1` (`connector_type_id`),
  CONSTRAINT `connector_type_property_fk1` FOREIGN KEY (`connector_type_id`) REFERENCES `connector_type` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `connector_type_property_k2` (`property_type_id`),
  CONSTRAINT `connector_type_property_fk2` FOREIGN KEY (`property_type_id`) REFERENCES `property_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `location_type`
--

CREATE TABLE `location_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `location_type_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `location`
--

CREATE TABLE `location` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `location_type_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `location_u1` (`name`),
  KEY `location_k1` (`location_type_id`),
  CONSTRAINT `location_fk1` FOREIGN KEY (`location_type_id`) REFERENCES `location_type` (`id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
--
-- Table `source`
--

DROP TABLE IF EXISTS `source`;
CREATE TABLE `source` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `source_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_state`
--

DROP TABLE IF EXISTS `component_state`;
CREATE TABLE `component_state` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_state_u1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component`
--

DROP TABLE IF EXISTS `component`;
CREATE TABLE `component` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `owner_user_id` int(11) unsigned DEFAULT NULL,
  `owner_group_id` int(11) unsigned DEFAULT NULL,
  `component_state_id` int(11) unsigned NOT NULL,
  `documentation_uri` varchar(256) DEFAULT NULL,
  `estimated_cost` float(10,2) DEFAULT NULL,
  `created_on_date_time` datetime NOT NULL,
  `created_by_user_id` int(11) unsigned NOT NULL,
  `modified_on_date_time` datetime NOT NULL,
  `modified_by_user_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_u1` (`name`),
  KEY `component_k1` (`component_state_id`),
  CONSTRAINT `component_fk1` FOREIGN KEY (`component_state_id`) REFERENCES `component_state` (`id`) ON UPDATE CASCADE,
  KEY `component_k2` (`owner_user_id`),
  CONSTRAINT `component_fk2` FOREIGN KEY (`owner_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `component_k3` (`owner_group_id`),
  CONSTRAINT `component_fk3` FOREIGN KEY (`owner_group_id`) REFERENCES `group` (`id`) ON UPDATE CASCADE,
  KEY `component_k4` (`created_by_user_id`),
  CONSTRAINT `component_fk4` FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `component_k5` (`modified_by_user_id`),
  CONSTRAINT `component_fk5` FOREIGN KEY (`modified_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `assembly_component`
--

DROP TABLE IF EXISTS `assembly_component`;
CREATE TABLE `assembly_component` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `assembly_id` int(11) unsigned NOT NULL,
  `component_id` int(11) unsigned NOT NULL,
  `quantity` int(11) unsigned DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `assembly_component_k1` (`assembly_id`),
  CONSTRAINT `assembly_component_fk1` FOREIGN KEY (`assembly_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `assembly_component_k2` (`component_id`),
  CONSTRAINT `assembly_component_fk2` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `collection`
--

DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `owner_user_id` int(11) unsigned DEFAULT NULL,
  `owner_group_id` int(11) unsigned DEFAULT NULL,
  `created_on_date_time` datetime NOT NULL,
  `created_by_user_id` int(11) unsigned NOT NULL,
  `modified_on_date_time` datetime NOT NULL,
  `modified_by_user_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `collection_u1` (`name`),
  KEY `collection_k1` (`owner_user_id`),
  CONSTRAINT `collection_fk1` FOREIGN KEY (`owner_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `collection_k2` (`owner_group_id`),
  CONSTRAINT `collection_fk2` FOREIGN KEY (`owner_group_id`) REFERENCES `group` (`id`) ON UPDATE CASCADE,
  KEY `collection_k3` (`created_by_user_id`),
  CONSTRAINT `collection_fk3` FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `collection_k4` (`modified_by_user_id`),
  CONSTRAINT `collection_fk4` FOREIGN KEY (`modified_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `collection_component`
--

DROP TABLE IF EXISTS `collection_component`;
CREATE TABLE `collection_component` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `collection_id` int(11) unsigned NOT NULL,
  `component_id` int(11) unsigned NOT NULL,
  `quantity` int(11) unsigned DEFAULT 1,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `collection_component_u1` (`collection_id`, `component_id`),
  KEY `collection_component_k1` (`collection_id`),
  CONSTRAINT `collection_component_fk1` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `collection_component_k2` (`component_id`),
  CONSTRAINT `collection_component_fk2` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_source`
--

DROP TABLE IF EXISTS `component_source`;
CREATE TABLE `component_source` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_id` int(11) unsigned NOT NULL,
  `source_id` int(11) unsigned NOT NULL,
  `part_number` varchar(64) DEFAULT NULL,
  `cost` float(10,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_source_u1` (`component_id`, `source_id`),
  KEY `component_source_k1` (`component_id`),
  CONSTRAINT `component_source_fk1` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `component_source_k2` (`source_id`),
  CONSTRAINT `component_source_fk2` FOREIGN KEY (`source_id`) REFERENCES `source` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_component_type`
--

DROP TABLE IF EXISTS `component_component_type`;
CREATE TABLE `component_component_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_id` int(11) unsigned NOT NULL,
  `component_type_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_component_type_u1` (`component_id`, `component_type_id`),
  KEY `component_component_type_k1` (`component_id`),
  CONSTRAINT `component_component_type_fk1` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `component_component_type_k2` (`component_type_id`),
  CONSTRAINT `component_component_type_fk2` FOREIGN KEY (`component_type_id`) REFERENCES `component_type` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_property`
--

DROP TABLE IF EXISTS `component_property`;
CREATE TABLE `component_property` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_id` int(11) unsigned NOT NULL,
  `property_type_id` int(11) unsigned NOT NULL,
  `value` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_property_u1` (`component_id`, `property_type_id`),
  KEY `component_property_k1` (`component_id`),
  CONSTRAINT `component_property_fk1` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `component_property_k2` (`property_type_id`),
  CONSTRAINT `component_property_fk2` FOREIGN KEY (`property_type_id`) REFERENCES `property_type` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_log`
--

DROP TABLE IF EXISTS `component_log`;
CREATE TABLE `component_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_id` int(11) unsigned NOT NULL,
  `log_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_log_u1` (`component_id`, `log_id`),
  KEY `component_log_k1` (`component_id`),
  CONSTRAINT `component_log_fk1` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `component_log_k2` (`log_id`),
  CONSTRAINT `component_log_fk2` FOREIGN KEY (`log_id`) REFERENCES `log` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_connector`
--

DROP TABLE IF EXISTS `component_connector`;
CREATE TABLE `component_connector` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_id` int(11) unsigned NOT NULL,
  `connector_type_id` int(11) unsigned NOT NULL,
  `label` varchar(64) DEFAULT NULL,
  `quantity` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_connector_u1` (`component_id`, `connector_type_id`, `label`),
  KEY `component_connector_k1` (`component_id`),
  CONSTRAINT `component_connector_fk1` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `component_connector_k2` (`connector_type_id`),
  CONSTRAINT `component_connector_fk2` FOREIGN KEY (`connector_type_id`) REFERENCES `connector_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_connector_resource`
--

DROP TABLE IF EXISTS `component_connector_resource`;
CREATE TABLE `component_connector_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_connector_id` int(11) unsigned NOT NULL,
  `resource_type_id` int(11) unsigned NOT NULL,
  `value` varchar(64) NOT NULL,
  `quantity` int(11) unsigned DEFAULT NULL,
  `is_provided` bool NOT NULL DEFAULT 0,
  `is_used_required` bool NOT NULL DEFAULT 0,
  `is_used_optional` bool NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_connector_resource_u1` (`component_connector_id`, `resource_type_id`, `value`),
  KEY `component_connector_resource_k1` (`component_connector_id`),
  CONSTRAINT `component_connector_resource_fk1` FOREIGN KEY (`component_connector_id`) REFERENCES `component_connector` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `component_connector_resource_k2` (`resource_type_id`),
  CONSTRAINT `component_connector_resource_fk2` FOREIGN KEY (`resource_type_id`) REFERENCES `resource_type` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Note that CHECK constraint is not supported in MySQL.
-- Hence, we need triggers to verify that at least one of 
-- is_used_required/optional is NULL
--
-- Older versions of MySQL do not support SIGNAL statement, so we simply
-- issue erroneous command
--
DELIMITER $$
DROP TRIGGER IF EXISTS `check_is_used_constraint1` $$
CREATE TRIGGER `check_is_used_constraint1` BEFORE INSERT ON `component_connector_resource` FOR EACH ROW 
  BEGIN
    IF (NEW.is_used_required <> 0 AND NEW.is_used_optional <> 0) THEN
      call raise_error('Constraint component_connector_resource.is_used violated: both fields is_used_required and is_used_optional cannot be set at the same time.');
    END IF; 
  END
$$
DROP TRIGGER IF EXISTS `check_is_used_constraint2` $$
CREATE TRIGGER `check_is_used_constraint2` BEFORE UPDATE ON `component_connector_resource` FOR EACH ROW 
  BEGIN
    IF (NEW.is_used_required <> 0 AND NEW.is_used_optional <> 0) THEN
      call raise_error('Constraint component_connector_resource.is_used violated: both fields is_used_required and is_used_optional cannot be set at the same time.');
    END IF; 
  END
$$
DELIMITER ;


--
-- Table `collection_log`
--

DROP TABLE IF EXISTS `collection_log`;
CREATE TABLE `collection_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `collection_id` int(11) unsigned NOT NULL,
  `log_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `collection_log_u1` (`collection_id`, `log_id`),
  KEY `collection_log_k1` (`collection_id`),
  CONSTRAINT `collection_log_fk1` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `collection_log_k2` (`log_id`),
  CONSTRAINT `collection_log_fk2` FOREIGN KEY (`log_id`) REFERENCES `log` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_instance`
--

DROP TABLE IF EXISTS `component_instance`;
CREATE TABLE `component_instance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_id` int(11) unsigned NOT NULL,
  `location_id` int(11) unsigned NOT NULL,
  `serial_number` varchar(16) DEFAULT NULL,
  `quantity` int(11) unsigned DEFAULT NULL,
  `created_on_date_time` datetime NOT NULL,
  `created_by_user_id` int(11) unsigned NOT NULL,
  `modified_on_date_time` datetime NOT NULL,
  `modified_by_user_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_instance_u1` (`component_id`, `location_id`),
  KEY `component_instance_k1` (`component_id`),
  CONSTRAINT `component_instance_fk1` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE,
  KEY `component_instance_k2` (`location_id`),
  CONSTRAINT `component_instance_fk2` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`) ON UPDATE CASCADE,
  KEY `component_instance_k3` (`created_by_user_id`),
  CONSTRAINT `component_instance_fk3` FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `component_instance_k4` (`modified_by_user_id`),
  CONSTRAINT `component_instance_fk4` FOREIGN KEY (`modified_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `component_instance_log`
--

DROP TABLE IF EXISTS `component_instance_log`;
CREATE TABLE `component_instance_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `component_instance_id` int(11) unsigned NOT NULL,
  `log_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `component_instance_log_u1` (`component_instance_id`, `log_id`),
  KEY `component_instance_log_k1` (`component_instance_id`),
  CONSTRAINT `component_instance_log_fk1` FOREIGN KEY (`component_instance_id`) REFERENCES `component_instance` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `component_instance_log_k2` (`log_id`),
  CONSTRAINT `component_instance_log_fk2` FOREIGN KEY (`log_id`) REFERENCES `log` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `design`
--

DROP TABLE IF EXISTS `design`;
CREATE TABLE `design` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `owner_user_id` int(11) unsigned DEFAULT NULL,
  `owner_group_id` int(11) unsigned DEFAULT NULL,
  `created_on_date_time` datetime NOT NULL,
  `created_by_user_id` int(11) unsigned NOT NULL,
  `modified_on_date_time` datetime NOT NULL,
  `modified_by_user_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `design_u1` (`name`),
  KEY `design_k1` (`owner_user_id`),
  CONSTRAINT `design_fk1` FOREIGN KEY (`owner_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `design_k2` (`owner_group_id`),
  CONSTRAINT `design_fk2` FOREIGN KEY (`owner_group_id`) REFERENCES `group` (`id`) ON UPDATE CASCADE,
  KEY `design_k3` (`created_by_user_id`),
  CONSTRAINT `design_fk3` FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `design_k4` (`modified_by_user_id`),
  CONSTRAINT `design_fk4` FOREIGN KEY (`modified_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `design_log`
--

DROP TABLE IF EXISTS `design_log`;
CREATE TABLE `design_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `design_id` int(11) unsigned NOT NULL,
  `log_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `design_log_u1` (`design_id`, `log_id`),
  KEY `design_log_k1` (`design_id`),
  CONSTRAINT `design_log_fk1` FOREIGN KEY (`design_id`) REFERENCES `design` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `design_log_k2` (`log_id`),
  CONSTRAINT `design_log_fk2` FOREIGN KEY (`log_id`) REFERENCES `log` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `design_component`
--

DROP TABLE IF EXISTS `design_component`;
CREATE TABLE `design_component` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `design_id` int(11) unsigned NOT NULL,
  `component_id` int(11) unsigned NOT NULL,
  `location_id` int(11) unsigned NOT NULL,
  `created_on_date_time` datetime NOT NULL,
  `created_by_user_id` int(11) unsigned NOT NULL,
  `modified_on_date_time` datetime NOT NULL,
  `modified_by_user_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `design_component_u1` (`name`, `design_id`),
  KEY `design_component_k1` (`design_id`),
  CONSTRAINT `design_component_fk1` FOREIGN KEY (`design_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `design_component_k2` (`component_id`),
  CONSTRAINT `design_component_fk2` FOREIGN KEY (`component_id`) REFERENCES `component` (`id`) ON UPDATE CASCADE,
  KEY `design_component_k3` (`location_id`),
  CONSTRAINT `design_component_fk3` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`) ON UPDATE CASCADE,
  KEY `design_component_k4` (`created_by_user_id`),
  CONSTRAINT `design_component_fk4` FOREIGN KEY (`created_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
  KEY `design_component_k5` (`modified_by_user_id`),
  CONSTRAINT `design_component_fk5` FOREIGN KEY (`modified_by_user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `design_component_log`
--

DROP TABLE IF EXISTS `design_component_log`;
CREATE TABLE `design_component_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `design_component_id` int(11) unsigned NOT NULL,
  `log_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `design_component_log_u1` (`design_component_id`, `log_id`),
  KEY `design_component_log_k1` (`design_component_id`),
  CONSTRAINT `design_component_log_fk1` FOREIGN KEY (`design_component_id`) REFERENCES `design_component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `design_component_log_k2` (`log_id`),
  CONSTRAINT `design_component_log_fk2` FOREIGN KEY (`log_id`) REFERENCES `log` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table `design_component_connection`
--

DROP TABLE IF EXISTS `design_component_connection`;
CREATE TABLE `design_component_connection` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `first_design_component_id` int(11) unsigned NOT NULL,
  `first_component_connector_id` int(11) unsigned NOT NULL,
  `second_design_component_id` int(11) unsigned NOT NULL,
  `second_component_connector_id` int(11) unsigned NOT NULL,
  `link_design_component_id` int(11) unsigned DEFAULT NULL,
  `link_design_component_quantity` int(11) unsigned DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `parent_design_component_connection_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `design_component_connection_k1` (`first_design_component_id`),
  CONSTRAINT `design_component_connection_fk1` FOREIGN KEY (`first_design_component_id`) REFERENCES `design_component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `design_component_connection_k2` (`first_component_connector_id`),
  CONSTRAINT `design_component_connection_fk2` FOREIGN KEY (`first_component_connector_id`) REFERENCES `component_connector` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `design_component_connection_k3` (`second_design_component_id`),
  CONSTRAINT `design_component_connection_fk3` FOREIGN KEY (`second_design_component_id`) REFERENCES `design_component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `design_component_connection_k4` (`second_component_connector_id`),
  CONSTRAINT `design_component_connection_fk4` FOREIGN KEY (`second_component_connector_id`) REFERENCES `component_connector` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `design_component_connection_k5` (`link_design_component_id`),
  CONSTRAINT `design_component_connection_fk5` FOREIGN KEY (`link_design_component_id`) REFERENCES `design_component` (`id`) ON UPDATE CASCADE,
  KEY `design_component_connection_k6` (`parent_design_component_connection_id`),
  CONSTRAINT `design_component_connection_fk6` FOREIGN KEY (`parent_design_component_connection_id`) REFERENCES `design_component_connection` (`id`) ON UPDATE CASCADE 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- We need triggers to verify that all design components belong to the
-- same design, and that connectors/design components belong to the
-- same component 
--

--
-- Table `assembly_component_connection`
--

DROP TABLE IF EXISTS `assembly_component_connection`;
CREATE TABLE `assembly_component_connection` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `first_assembly_component_id` int(11) unsigned NOT NULL,
  `first_component_connector_id` int(11) unsigned NOT NULL,
  `second_assembly_component_id` int(11) unsigned NOT NULL,
  `second_component_connector_id` int(11) unsigned NOT NULL,
  `link_assembly_component_id` int(11) unsigned DEFAULT NULL,
  `link_assembly_component_quantity` int(11) unsigned DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `assembly_component_connection_k1` (`first_assembly_component_id`),
  CONSTRAINT `assembly_component_connection_fk1` FOREIGN KEY (`first_assembly_component_id`) REFERENCES `assembly_component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `assembly_component_connection_k2` (`first_component_connector_id`),
  CONSTRAINT `assembly_component_connection_fk2` FOREIGN KEY (`first_component_connector_id`) REFERENCES `component_connector` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `assembly_component_connection_k3` (`second_assembly_component_id`),
  CONSTRAINT `assembly_component_connection_fk3` FOREIGN KEY (`second_assembly_component_id`) REFERENCES `assembly_component` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `assembly_component_connection_k4` (`second_component_connector_id`),
  CONSTRAINT `assembly_component_connection_fk4` FOREIGN KEY (`second_component_connector_id`) REFERENCES `component_connector` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  KEY `assembly_component_connection_k5` (`link_assembly_component_id`),
  CONSTRAINT `assembly_component_connection_fk5` FOREIGN KEY (`link_assembly_component_id`) REFERENCES `assembly_component` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- We need triggers to verify that all assembly components belong to the
-- same assembly, and that connectors/assembly components belong to the
-- same component 
--
