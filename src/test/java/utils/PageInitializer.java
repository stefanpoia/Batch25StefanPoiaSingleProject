
package utils;

import pages.ContactDetailsPage;
import pages.DashboardPage;
import pages.LoginPage;

public class PageInitializer {

    public static DashboardPage dashboardPage;
    public static LoginPage loginPage;
    public static ContactDetailsPage contactDetailsPage;
    public static void initializePageObjects(){
        dashboardPage = new DashboardPage();
        loginPage = new LoginPage();
        contactDetailsPage = new ContactDetailsPage();


    }
}
