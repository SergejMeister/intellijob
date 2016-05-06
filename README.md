# intellijob
intelligent job search

##This service:
Scans your mail box to find out mails from jobs portal (monster.de, stepstone.de)!
Parses mail context to determine jobs metadata (contact person, mail, address, requirements, skills etc.)!
Uses users profile metadata to find out the best matching job offer!

###HOW to build a run a new intellijob.jar file on LINUX!

* install java 1.8
* install maven

###DOWNLOAD-Civis-Tools
* git clone https://github.com/SergejMeister/civis-tools.git -b master
* cd civis-tools/ && mvn clean install && cd ..

###DOWNLOAD-IntelliJob
* git clone https://github.com/SergejMeister/intellijob.git -b master
* cd intellijob/ && mvn clean install

###Run
* java -jar target/intellijob-1.2.0.jar


##DEMO
* You can find a demo version in /demo
* Please extract the archiv demo.zip
* run java -jar intellijob-1.2.0.jar



