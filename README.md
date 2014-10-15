Building Your Own Dataloader for Salesforce

This project is all about using Saleforce API’s + Core Java + Embedded DB = Build Your Own Dataloader.

For details regarding the project please download the project and have a look at the doc folder, there will be a presentation with explanation on what this project is all about.

Note: - This is just a sample App, created for use cases mentioned in my presentation but it has all the potential to turn this in to a tool which fits your Organization.

Do try this @ your office. :)

Step 1:

Fill the properties file with relevant details, (/conf/application.properties)

sfdc.username=your salesforce user login
sfdc.password=your salesforce user password
sfdc.stoken=your security token
sfdc.appurl=https://login.salesforce.com //Can be changed to test to load data in to your Sandbox.
sfdc.managedPackage=false
sfdc.partnerUrl=https://login.salesforce.com/services/Soap/u/29.0
sfdc.metadataUrl=https://login.salesforce.com/services/Soap/u/29.0
sfdc.apexUrl=https://login.salesforce.com/services/Soap/s/29.0

Step 2:

Create your own ETL job config, (/resources/jobs)

There are sample job file kept in this location, it’s straight forward to fill this information and please go through the code to look up what all properties I am using currently since I have put additional information or keys for future expansion.

Step 3:

Start reading the code. A good starting point would be,

* SFDCConnection.java --> Code for different types of connection (Partner, Metadata, Apex).
* com.dreamforce.demo.sfdc.api --> In this package, I have placed the code dealing with SFDC Bulk/Rest Api
* com.dreamforce.demo.db --> H2 DB operations
* App.java --> Global class to initialize project and hold static members.
* ETLJobProcessor.java --> Code having the main execution context and also all the procession related to ETL is placed in here.

Step 4:

Execute the required job defined in the JSON from ETLJobProcessor.java (main method).

Step 5:

Look out for Dreamforce.log or console for progress.

“Beneath this project there is an idea and ideas are bulletproof”





