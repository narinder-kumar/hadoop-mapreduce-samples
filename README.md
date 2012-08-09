hadoop-mapreduce-samples
========================

Sample Programs representing different capabilities of Map Reduce Framework

User-access-log parser
========================

Program is designed to parse user access log files and provide details about user's accessing the system within a given time frame.

Expectations
============

Program expects user-log files to be of following format

<user-name/id> <accesstime in System's current milliseconds>

Sample log files are provided in logfiles directory

Logfiles need to be copied to Hadoop's HDFS file system to work upon.


Pre-requisites
==============

Working Installation of Hadoop. The program has been tested on Hadoop 1.0.3

Execution Steps
===============

1. Compile the source file com.mercris.hadoop.mapreduce.samples.useraccess.UserAccessCount. It will require hadoop-core-xxx.jar and hadoop-client-xxx.jar in the CLASSPATH. These jars are part of hadoop distribution. An easy way to do would be to checkout the source code and create a Java project in your preferred IDE like Eclipse and Intellij. Configuring CLASSPATH of your project will compile all the source code automatically for you.

2. Create a JAR file of the compiled .class files. A simple shell script makejar.sh is available inside utils folder. It assumes all the class files are located inside bin folder. You may need to change it appropriately.

3. Copy user-access log files to Hadoop file-system.

4. Run the program using utils/runaccesslog.sh after making necessary changes related to your local environment. Following are the relevant parameters you may need to change:

    a. Root folder location in HDFS file-system where log files are copied

    b. Expected folder location in HDFS file-system where results will be stored. Note that the folder should not exist before-hand
    
    c. Start time (in milliseconds) from where, the program should take into account user-access

    d. End time (in milliseconds) till where, the program should take into account user-access

5. Access output at specified <output-folder>/part-00000 file in your HDFS file system. Output folder is specified in utils/runaccesslog.sh.

Sample Output
=============

user10  1

user11	1

user12	1

user13	1

user14	1

user4	2

user5	2

user6	2

user7	1

user8	1

user9	1

Understanding result
====================

During the time limits provided to the program, above mentioned users have accessed the system number of times mentioned along with their name/id.