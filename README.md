# Selenium- Steps to install

1. Download and Install Java: you can download the latest version of Java Development Kit (JDK) from the link given below.
http://www.oracle.com/technetwork/java/javase/downloads/index.html
2. Download and Configure Eclipse IDE
3. Download Selenium WebDriver Java Client
4. Configure Selenium WebDriver

For References, you can vist https://www.guru99.com/installing-selenium-webdriver.html

# TestNG- Steps to install

Following is a step by step guide on how to install TestNG in Eclipse:
1. Open Eclipse software & install new software
2. Search the TestNG using the Find option and Click on the installation button.
3. Confirm the installation process.
4. Accept the license agreement
5. After accept the licence agreement, wait for installation.
6. Ignore security warning if occurs
7. Finish the installation and restart your system
8. Verify if the installation is done properly

For References, you can vist https://www.guru99.com/install-testng-in-eclipse.html

# Coding Convention

1. Coding Standard and Naming conventions to be followed in automation framework

|S.No|Type|Rules for Naming|Examples|
| --------------- | --------------- | --------------- | --------------- |
|1|Packages|A package should be named in lowercase characters|com,org,testcase|
|2|Methods|Method name should be a verb in lower camel case|bookNowOldUser()|
|3|Classes|Class Name should be in upper camel case|ExcelReader|
|4|Constructors|"Same as for the Class name. It does not have any return type that's why we never write anything like void/any primitive data type as return type."|
|5|Variables|"It should start with lower case and if the variable is made up of more than one word then the first letter of each inner word would be in Upper Case separated by an underscore( _ ). A variable name should be short which clearly defines the purpose of the variable."|String rider_First_Name;|
|6|Pages|Add suffix 'Page' with all pages you develop. It will segregate Page classes from other available classes. The page name should be as per the title of the webpage with the upper camel case."|The title of the home page is 'Home'. You should create a page as HomePage.java.|
|7|Web element|Web element name should be given as it is shown on UI and in lower camel case|userName field|
|8|Test Case Naming|"Test Case naming format should be as mentioned below: RA_TestCaseID_FeatureName.java"|RA_2341_Create_Ride.java|
|9|Properties file|There should be a separate property file for each page. The name of a property file should be the same as that of Page name ending with .properties.|HomePage.properties|
|10|Comments|"For single-line comment, one can use '//' For multiline, use block comments'|'Single line: //this is one variable of integer typeMultiline: /* * Here is a block comment. */'|
