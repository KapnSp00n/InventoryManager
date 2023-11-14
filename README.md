"# InventoryManager
   This inventory manager app developed mostly on Java make use of Java for backend while 
   using JavaFX and SceneBuilder for UI. The logIn system goes through a locally hosted ]
   MySQL server. Here are some of the important MYSQL procedures if you intend to replicate 
   something similar
   'addUser' procedure (This is called in the Java code to try registering an account"
    CREATE DEFINER=`root`@`localhost` PROCEDURE `addUser`(
		username varchar(30),
    	pass varchar (30),
    	userdata MEDIUMBLOB)
	Begin
		DECLARE salty VARCHAR(6);
    	SET salty = SUBSTRING(SHA1(RAND()), 1, 6);
		INSERT INTO users 
    	VALUES (username, SHA2(CONCAT(pass,salty),256), salty, userdata);
	END
   'logIn' procedure
	CREATE DEFINER=`root`@`localhost` PROCEDURE `logInTime`(
		username varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs,
    	password1 varchar (30)
		)
	BEGIN
		DECLARE salty varchar(6);
    	DECLARE pass varchar(256);
    	DECLARE object MEDIUMBLOB;
    	SET pass = (select pass_word_hash FROM users WHERE user_name = username);
    	SET object = (select user_data FROM users WHERE user_name = username);
    	SET salty = (select salt FROM users WHERE user_name = username);
    	IF (SHA2(CONCAT(password1,salty),256)=pass) THEN
			SELECT object;
		ELSE 
			SELECT null;
    	END IF;
	END
" 
