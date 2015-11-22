# intellijob
intelligent job search

##This service:
Scans your mail box to find out mails from jobs portal (monster.de, stepstone.de)!
Parses mail context to determine jobs metadata (contact person, mail, address, requirements, skills etc.)!
Uses jobs metadata to find out the best matching job offer!

##HOW to start on LINUX

*install java 1.8
*install mongodb
*run mongodb

##DOWNLOAD-Civis-Tools
*git clone https://github.com/SergejMeister/civis-tools.git -b master
*cd civis-tools/ && mvn clean install && cd ..

##DOWNLOAD-IntelliJob
*git clone https://github.com/SergejMeister/intellijob.git -b master
*cd intellijob/ && mvn clean install

##Run
*java -jar target/intellijob-1.0.0.jar


