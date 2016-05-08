cd ../src/database/data-log
echo 'New Run' >> hsqldb.log
date >> hsqldb.log
echo 'Server is running in the background...'
java -cp ../hsqldb.jar org.hsqldb.server.Server >> hsqldb.log

