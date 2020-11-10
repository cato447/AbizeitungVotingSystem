-- MySQL dump 10.13  Distrib 8.0.22, for Linux (x86_64)
--
-- Host: localhost    Database: VotingSystem
-- ------------------------------------------------------
-- Server version	8.0.22-0ubuntu0.20.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `candidates`
--

DROP TABLE IF EXISTS `candidates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `candidates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `votes` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKahv5ay5uut18brh5v69sfeggh` (`category_id`),
  CONSTRAINT `FKahv5ay5uut18brh5v69sfeggh` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidates`
--

LOCK TABLES `candidates` WRITE;
/*!40000 ALTER TABLE `candidates` DISABLE KEYS */;
INSERT INTO `candidates` VALUES (1,'Greta Bentgens',0,1),(2,'Laura König',0,1),(3,'Aaron Glos',0,1),(4,'Lukas Boy',0,1),(5,'Frau Meyering',0,1),(6,'Frau Adams',0,1),(7,'Herr Petering',0,1),(8,'Frau Milde',0,1),(9,'Frau Meyer',0,1);
/*!40000 ALTER TABLE `candidates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Beste/r Schüler/in'),(2,'Beste/r Lehrer/in');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (13),(13);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `voters`
--

DROP TABLE IF EXISTS `voters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `voters` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `vote_status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `voters`
--

LOCK TABLES `voters` WRITE;
/*!40000 ALTER TABLE `voters` DISABLE KEYS */;
INSERT INTO `voters` VALUES (1,'negar.barzegar@adolfinum.de',_binary '\0'),(2,'simon.berger@adolfinum.de',_binary '\0'),(3,'nicolas.cunha@adolfinum.de',_binary '\0'),(4,'joline.hackstein@adolfinum.de',_binary '\0'),(5,'philip.heckhoff@adolfinum.de',_binary ''),(6,'simon.hormes@adolfinum.de',_binary '\0'),(7,'benjamin.vogt@adolfinum.de',_binary '\0'),(8,'taycan.arslan@adolfinum.de',_binary '\0'),(9,'luca.boom@adolfinum.de',_binary '\0'),(10,'leon.borgerding@adolfinum.de',_binary '\0'),(11,'lukas.boy@adolfinum.de',_binary '\0'),(12,'lukas.corell@adolfinum.de',_binary '\0'),(13,'cedric.damerow@adolfinum.de',_binary '\0'),(14,'amelie.david@adolfinum.de',_binary '\0'),(15,'zeynep.efe@adolfinum.de',_binary '\0'),(16,'melinda.hirschelmann@adolfinum.de',_binary '\0'),(17,'lars.hogardt@adolfinum.de',_binary '\0'),(18,'malin.kalnins@adolfinum.de',_binary '\0'),(19,'victor.kocksnchez@adolfinum.de',_binary '\0'),(20,'kamil.kowalczyk@adolfinum.de',_binary '\0'),(21,'romyna.gurny@adolfinum.de',_binary '\0'),(22,'noelia.kocksnchez@adolfinum.de',_binary '\0'),(23,'lili.schweitzer@adolfinum.de',_binary '\0'),(24,'jerome.laukamp@adolfinum.de',_binary '\0'),(25,'elisa.bahl@adolfinum.de',_binary '\0'),(26,'lisa.baumeister@adolfinum.de',_binary '\0'),(27,'johanna.boeckmann@adolfinum.de',_binary '\0'),(28,'svenja.fischer@adolfinum.de',_binary '\0'),(29,'lena.goehlich@adolfinum.de',_binary '\0'),(30,'paula.haub@adolfinum.de',_binary '\0'),(31,'daria.horstmann@adolfinum.de',_binary '\0'),(32,'melina.kascha@adolfinum.de',_binary '\0'),(33,'pia.kleinwegen@adolfinum.de',_binary '\0'),(34,'lauramarie.koenig@adolfinum.de',_binary '\0'),(35,'yarkin.kulaksiz@adolfinum.de',_binary '\0'),(36,'amelie.laake@adolfinum.de',_binary '\0'),(37,'noemi.malaponti@adolfinum.de',_binary '\0'),(38,'yara.mueser@adolfinum.de',_binary '\0'),(39,'paul.nowack@adolfinum.de',_binary '\0'),(40,'luca.ofiera@adolfinum.de',_binary '\0'),(41,'timo.otto@adolfinum.de',_binary '\0'),(42,'linnea.paulukuhn@adolfinum.de',_binary '\0'),(43,'isabelle.schneider@adolfinum.de',_binary '\0'),(44,'nico.scholzen@adolfinum.de',_binary '\0'),(45,'manon.schroff@adolfinum.de',_binary '\0'),(46,'carlotta.tueckmantel@adolfinum.de',_binary '\0'),(47,'simon.bussmann@adolfinum.de',_binary '\0'),(48,'luis.erpenbach@adolfinum.de',_binary '\0'),(49,'meret.fass@adolfinum.de',_binary '\0'),(50,'anna.feldmann@adolfinum.de',_binary '\0'),(51,'alina.fuenderich@adolfinum.de',_binary '\0'),(52,'joline.gilles@adolfinum.de',_binary '\0'),(53,'karolina.hein@adolfinum.de',_binary '\0'),(54,'robin.heldt@adolfinum.de',_binary '\0'),(55,'annika.koch@adolfinum.de',_binary '\0'),(56,'dusanka.djukanovic@adolfinum.de',_binary '\0'),(57,'aaron.glos@adolfinum.de',_binary '\0'),(58,'ayseguel.guelten@adolfinum.de',_binary '\0'),(59,'hamza.hasoumi@adolfinum.de',_binary '\0'),(60,'evelyn.hofmann@adolfinum.de',_binary '\0'),(61,'burakmustafa.kulac@adolfinum.de',_binary '\0'),(62,'dominik.kwitowski@adolfinum.de',_binary '\0'),(63,'julia.lener@adolfinum.de',_binary '\0'),(64,'paula.may@adolfinum.de',_binary '\0'),(65,'luca.mueller@adolfinum.de',_binary '\0'),(66,'mathieu.mueller@adolfinum.de',_binary '\0'),(67,'marie.puetter@adolfinum.de',_binary '\0'),(68,'hendrik.herffs@adolfinum.de',_binary '\0'),(69,'greta.bentgens@adolfinum.de',_binary '\0'),(70,'sven.mittmann@adolfinum.de',_binary '\0'),(71,'jan.hoevel@adolfinum.de',_binary '\0'),(72,'tim.krichel@adolfinum.de',_binary '\0'),(73,'milo.lehnen@adolfinum.de',_binary '\0'),(74,'lewis.lehner@adolfinum.de',_binary '\0'),(75,'nico.lipinski@adolfinum.de',_binary '\0'),(76,'luise.lu@adolfinum.de',_binary '\0'),(77,'maike.nawarotzky@adolfinum.de',_binary '\0'),(78,'rabea.peters@adolfinum.de',_binary '\0'),(79,'patrick.preuss@adolfinum.de',_binary '\0'),(80,'julius.preusser@adolfinum.de',_binary '\0'),(81,'marie.scheidung@adolfinum.de',_binary '\0'),(82,'lena.schlayer@adolfinum.de',_binary '\0'),(83,'emma.sprenger@adolfinum.de',_binary '\0'),(84,'klaudia.kapala@adolfinum.de',_binary '\0'),(85,'gabriel.schacht@adolfinum.de',_binary '\0'),(86,'delia.schmitz@adolfinum.de',_binary '\0'),(87,'katharina.schmitz@adolfinum.de',_binary '\0'),(88,'laurin.severith@adolfinum.de',_binary '\0'),(89,'julian.sievers@adolfinum.de',_binary '\0'),(90,'anna.siewert@adolfinum.de',_binary '\0'),(91,'chiara.welter@adolfinum.de',_binary '\0'),(92,'kira.winzen@adolfinum.de',_binary '\0'),(93,'tim.zentzis@adolfinum.de',_binary '\0'),(94,'justus.boesken@adolfinum.de',_binary '\0'),(95,'finia.brinkmann@adolfinum.de',_binary '\0'),(96,'anesa.cavcic@adolfinum.de',_binary '\0'),(97,'antonia.eigemann@adolfinum.de',_binary '\0'),(98,'nico.hahn@adolfinum.de',_binary '\0'),(99,'timo.kohlmann@adolfinum.de',_binary '\0'),(100,'alexander.kupillas@adolfinum.de',_binary '\0'),(101,'alexander.neumann@adolfinum.de',_binary '\0'),(102,'sophie.osterloh@adolfinum.de',_binary '\0'),(103,'clemens.palinsky@adolfinum.de',_binary '\0'),(104,'oliver.palinsky@adolfinum.de',_binary '\0'),(105,'hendrik.pierlo@adolfinum.de',_binary '\0'),(106,'lilly.schmidtke@adolfinum.de',_binary '\0'),(107,'mara.spicker@adolfinum.de',_binary '\0'),(108,'anhtrung.vo@adolfinum.de',_binary '\0'),(109,'ben.schwarz@adolfinum.de',_binary '\0'),(110,'luca.urbanczyk@adolfinum.de',_binary '\0'),(111,'helena.neukirch@adolfinum.de',_binary '\0'),(112,'nikita.lauff@adolfinum.de',_binary '\0'),(113,'jennifer.lengard@adolfinum.de',_binary '\0'),(114,'julia.mueller@adolfinum.de',_binary '\0'),(115,'philipp.nothers@adolfinum.de',_binary '\0'),(116,'judith.oppenberg@adolfinum.de',_binary '\0'),(117,'dilan.oeztuerk@adolfinum.de',_binary '\0'),(118,'malo.soulier@adolfinum.de',_binary '\0'),(119,'mery.stern@adolfinum.de',_binary '\0'),(120,'nouel.verberkt@adolfinum.de',_binary '\0'),(121,'leon.viktora@adolfinum.de',_binary '\0'),(122,'pia.anthes@adolfinum.de',_binary '\0'),(123,'eray.arici@adolfinum.de',_binary '\0'),(124,'christian.beutel@adolfinum.de',_binary '\0'),(125,'mara.blanke@adolfinum.de',_binary '\0'),(126,'lilly.ventzke@adolfinum.de',_binary '\0'),(127,'luzi.weichert@adolfinum.de',_binary '\0'),(128,'moritz.weihnacht@adolfinum.de',_binary '\0'),(129,'leony.wittmann@adolfinum.de',_binary '\0'),(130,'annika.lieblang@adolfinum.de',_binary '\0'),(131,'leonie.wallusch@adolfinum.de',_binary '\0'),(132,'felix.kirsten@adolfinum.de',_binary '\0'),(133,'moritz.liebisch@adolfinum.de',_binary '\0'),(134,'christian.pickardt@adolfinum.de',_binary '\0'),(135,'jan.schliekmann@adolfinum.de',_binary '\0'),(136,'elsa.piplack@adolfinum.de',_binary '\0'),(137,'jolan.gerritzen@adolfinum.de',_binary '\0'),(138,'lorena.garau@adolfinum.de',_binary '\0'),(139,'matthias.karl@adolfinum.de',_binary '\0'),(140,'justin.kauschke@adolfinum.de',_binary '\0'),(141,'leonie.kramer@adolfinum.de',_binary '\0'),(142,'laura.kurreck@adolfinum.de',_binary '\0'),(143,'maya.lueck@adolfinum.de',_binary '\0'),(144,'sean.mccormick-silex@adolfinum.de',_binary '\0'),(145,'tim.mueller@adolfinum.de',_binary '\0'),(146,'lana.peric@adolfinum.de',_binary '\0'),(147,'jan.pintostrohhaeusl@adolfinum.de',_binary '\0'),(148,'laura.ruettershoff@adolfinum.de',_binary '\0'),(149,'charlotte.schirmer@adolfinum.de',_binary '\0'),(150,'lavinia.schmitz@adolfinum.de',_binary '\0'),(151,'victor.schroers@adolfinum.de',_binary '\0'),(152,'gerrit.schulz@adolfinum.de',_binary '\0'),(153,'clemens.spoo@adolfinum.de',_binary '\0'),(154,'simon.stavroulakis@adolfinum.de',_binary '\0'),(155,'ioannis.boerner@adolfinum.de',_binary '\0'),(156,'marwa.nafouti@adolfinum.de',_binary '\0');
/*!40000 ALTER TABLE `voters` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-10 23:26:50
