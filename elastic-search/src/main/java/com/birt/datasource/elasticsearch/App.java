package com.birt.datasource.elasticsearch;

/**
 * You can run this to test your code changes without using BIRT!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Aggregation ea = new Aggregation();
        
        String responseJson = ea.getAggregation("localhost", 9300, "form_submissions", "605287", "customer", "Inspector Name");

        System.out.println(responseJson);
    }
}