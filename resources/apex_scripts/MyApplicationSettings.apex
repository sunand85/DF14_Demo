MyApplicationSettings__c appSettings = new MyApplicationSettings__c();

appSettings.AggregationType__c = 'ACCOUNTLEVEL';
appSettings.Granularity__c = 'WEEKLY';
appSettings.WeekStartsOn__c = 'Sat';
appSettings.MeasureColMap__c = '{"Downloads":"Downloads__c","Units":"Units__c","Amount":"Amount__c"}';
insert appSettings;