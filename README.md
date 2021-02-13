# AbizeitungVotingSystem
This project is a custom web server constructed to simplify the voting process for our senior book <br>
All private data got removed <br>
You are free to use this web server <br>
Be aware that this webserver isn't really securly build. To avoid complications use a DNS mask

# Requierments
MySql

# Installation
Clone the repo <br>
Run `cd AbizeitungVotingSystem` <br>
Add your mysql database credentials and email credentials to the application.properties file <br>
Run `mvn install -DskipTests`<br>

The .jar is located in the target folder<br>
Run the .jar as a deamon on your machine <br>

Webserver is reachable at host_ip:8000 <br>
(Check if your firewall allows traffic over port 8000 if website is not reachable)

# Usage
Add your email whitelist to the file `AbizeitungVotingSystem/src/main/resources/Email_Whitelist.txt` <br>
Add your Categories to the file `AbizeitungVotingSystem/src/main/resources/Categories.txt` <br>
Adjust the regex in `VotingController.java` and `start.html` to suit your needs <br>
<br>
To alternate between adding and voting mode with the two coresponding booleans in the `VotingController.java` file

# Contribution
Feel free to fork the repo and submit a pull-request. I will review and add it happily :)
