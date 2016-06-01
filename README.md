# Stock
This project is an exercise to get familiar with RIA.
For this aim we used:
- Maven
- Angular.js

#Json-Server
visit (https://github.com/typicode/json-server)

- Install Node.JS
	* sudo apt-get install nodejs
- install the node package manager
	* sudo apt-get install npm
- Create a symbolic link for node
	* sudo ln -s /usr/bin/nodejs /usr/bin/node
- Install Json-Server
	* sudo npm install -g json-server
- Goto database directory and run the server
	* json-server --watch db.json --port 3000

#Tomcat
visit (http://tecadmin.net/install-tomcat-9-on-ubuntu)
visit (https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-14-04)

	$ java -version
	$ cd /opt
	$ wget http://www.us.apache.org/dist/tomcat/tomcat-9/v9.0.0.M4/bin/apache-tomcat-9.0.0.M4.tar.gz
	$ tar xzf apache-tomcat-9.0.0.M4.tar.gz
	$ mv apache-tomcat-9.0.0.M4 tomcat9
	$ echo "export CATALINA_HOME="/opt/tomcat9"" >> /etc/environment
	$ echo "export JAVA_HOME="/usr/lib/jvm/java-8-oracle"" >> /etc/environment
	$ echo "export JRE_HOME="/usr/lib/jvm/java-8-oracle/jre"" >> /etc/environment
	$ source ~/.bashrc
	$ sudo chgrp -R tomcat conf
	$ sudo chmod g+rwx conf
	$ sudo chmod g+r conf/*
	$ sudo chown -R tomcat work/ temp/ logs/
	$ sudo update-alternatives --config java
	  Edit conf/tomcat-users.xml file in your editor and paste inside <tomcat-users> </tomcat-users> tags.
		<!-- user manager can access only manager section -->
		<role rolename="manager-gui" />
		<user username="manager" password="_SECRET_PASSWORD_" roles="manager-gui" />

		<!-- user admin can access manager and admin section both -->
		<role rolename="admin-gui" />
		<user username="admin" password="_SECRET_PASSWORD_" roles="manager-gui,admin-gui" />
	$ cd /opt/tomcat9
	$ ./bin/startup.sh
