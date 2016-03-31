LOCK TABLES `component_type` WRITE;
/*!40000 ALTER TABLE `component_type` DISABLE KEYS */;
INSERT INTO `component_type` VALUES
(4,'Table',NULL,2),
(5,'Rack',NULL,2),
(6,'Cabinet',NULL,2),
(7,'Enclosure',NULL,2),
(8,'Card Cage',NULL,2),
(9,'Instrumentation Component',NULL,3),
(10,'Controller - Generic',NULL,3),
(11,'Controller - Ion Pump',NULL,3),
(12,'Controller - Gate Valve',NULL,3),
(13,'Controller - Vacuum Gauge',NULL,3),
(14,'Controller - Heat tape',NULL,3),
(15,'Controller - PID',NULL,3),
(16,'Controller - Temperature',NULL,3),
(17,'Controller - Motor',NULL,3),
(18,'Controller - Power Supply',NULL,3),
(19,'Controller - PLC',NULL,3),
(20,'Controller - Water flow',NULL,3),
(21,'Controller - RGA',NULL,3),
(22,'Monitoring System',NULL,3),
(23,'Gauge/Sensor - strain',NULL,3),
(24,'Gauge/Sensor  - vacuum',NULL,3),
(25,'Gauge/Sensor  - thermocouple',NULL,3),
(26,'Gauge/Sensor  - RTD',NULL,3),
(27,'Gauge/Sensor  - pressure',NULL,3),
(28,'Gauge/Sensor - waterflow',NULL,3),
(29,'Gauge/Sensor - RGA',NULL,3),
(30,'Motor',NULL,3),
(31,'Motor - Driver',NULL,3),
(32,'Motor - Position Monitor',NULL,3),
(33,'Motor - Limit Switch',NULL,3),
(34,'Cable',NULL,3),
(35,'Patch Panel',NULL,3),
(36,'Adapter',NULL,3),
(37,'Module',NULL,3),
(38,'Blackbox',NULL,3),
(39,'ADC',NULL,3),
(40,'DAC',NULL,3),
(41,'Discrete I/O',NULL,3),
(42,'CPU',NULL,3),
(43,'FPGA',NULL,3),
(44,'Oscilloscope/DSA',NULL,3),
(45,'Counter',NULL,3),
(46,'Function Generator',NULL,3),
(47,'Frequency Synthesizer',NULL,3),
(48,'Voltmeter',NULL,3),
(49,'Power Supply',NULL,3),
(50,'Amplifier',NULL,3),
(51,'Multiplexor',NULL,3),
(52,'Interlock',NULL,3),
(53,'Readout/Display',NULL,3),
(54,'Controls Component',NULL,3),
(55,'Network',NULL,3),
(56,'Timing',NULL,3),
(57,'IOC',NULL,3),
(58,'Computer - Server/Workstation','',3),
(59,'Video',NULL,3),
(60,'Interface Adapter',NULL,3),
(61,'Accelerator Component',NULL,5),
(62,'Girder',NULL,5),
(63,'Stand',NULL,5),
(64,'Vacuum Chamber',NULL,5),
(65,'Transition Piece',NULL,5),
(66,'Vacuum Pump',NULL,5),
(67,'Absorber',NULL,5),
(68,'Heat Tape',NULL,5),
(69,'Flag',NULL,5),
(70,'Scraper',NULL,5),
(71,'Bellows',NULL,5),
(72,'Assembly',NULL,5),
(73,'Vacuum Flange',NULL,5),
(74,'Vacuum Seal',NULL,5),
(75,'Vacuum Valve',NULL,5),
(76,'Fastener',NULL,5),
(77,'Water line',NULL,5),
(78,'Water fitting',NULL,5),
(79,'Water seal',NULL,5),
(80,'Magnet Component',NULL,6),
(81,'Trim',NULL,6),
(82,'Dipole',NULL,6),
(83,'Quadrupole','',6),
(84,'Sextupole','',6),
(85,'PS Component',NULL,7),
(86,'Diagnostic Component',NULL,8),
(87,'BPM',NULL,8),
(88,'Loss Monitor',NULL,8),
(89,'Current Monitor',NULL,8),
(90,'Screen',NULL,8),
(91,'Optics',NULL,8),
(92,'RF Component',NULL,9),
(93,'Cavity/accelerating structure',NULL,9),
(94,'Phase shifter',NULL,9),
(95,'Attenuator',NULL,9),
(96,'coupler',NULL,9),
(97,'Envelope detector',NULL,9),
(98,'Phase monitor/detector',NULL,9),
(99,'Klystron',NULL,9),
(100,'HVPS',NULL,9),
(101,'Splitter',NULL,9),
(102,'RF Source',NULL,9),
(103,'Circulator',NULL,9),
(104,'Beamline Component',NULL,10),
(105,'Insertion Device Component',NULL,11),
(107,'Gauge/Sensor - accelerometer',NULL,3),
(108,'Fanout',NULL,3),
(109,'Signal Converter',NULL,3),
(111,'Filter',NULL,3),
(112,'Mask','',10),
(113,'KQUAD',NULL,12),
(114,'MONI',NULL,12),
(115,'CSBEND',NULL,12),
(116,'VERTEX-POINT',NULL,12),
(117,'KSEXT','',12),
(118,'Plate',NULL,5),
(119,'Plinth',NULL,5),
(120,'Support',NULL,5),
(121,'Computer - Laptop/Tablet','',3),
(122,'High Voltage Pulser','',7),
(123,'High Voltage Attenuator','',7),
(124,'Flash Disk','',3),
(125,'BPM Processor','Takes BPM signals, converts to X/Y/Sum',8),
(126,'MARK','',12),
(127,'Fan','',1),
(128,'Measurement System','',3),
(129,'Data Logging System','',3),
(130,'Gateway/Protocol Converter','',3),
(131,'Current Transformer','',7),
(132,'Window','',10),
(133,'Labyrinth','',10),
(134,'Survey Port','',10),
(135,'Shutter','',10),
(136,'Shielding','',10),
(137,'Guillotine','',10),
(138,'Access Door','',10),
(139,'Beam Stop','',10),
(140,'Beam Transport','',10),
(141,'Collimator','',10);
/*!40000 ALTER TABLE `component_type` ENABLE KEYS */;
UNLOCK TABLES;
