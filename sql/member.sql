CREATE TABLE `member` (
  `memNo` int NOT NULL AUTO_INCREMENT,
  `memId` varchar(20) NOT NULL,
  `memPw` varchar(65) NOT NULL,
  `memNm` varchar(30) NOT NULL,
  `socialChannel` enum('none','Naver','Kakao') DEFAULT 'none',
  `socialId` varchar(65) DEFAULT NULL,
  `regDt` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`memNo`),
  UNIQUE KEY `memId` (`memId`)
);