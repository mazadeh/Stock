if [ -z "$CATALINA_HOME" ]; then
    echo "The environment variable CATALINA_HOME must be set to the root of the Tomcat installation directory"
    exit 1
fi  

APP_CONTEXT=stock

sudo cp target/$APP_CONTEXT.war $CATALINA_HOME/webapps/
