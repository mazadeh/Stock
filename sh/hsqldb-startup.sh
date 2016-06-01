cd ../src/database

if [ -d data-log ]
then
	echo Database exists
else
	echo Database created
	mkdir -p data-log
fi
cd data-log

echo 'New Run' >> hsqldb.log
date >> hsqldb.log
echo 'Server is running in the background...'
java -cp ../hsqldb.jar org.hsqldb.server.Server >> hsqldb.log

