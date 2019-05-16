// SuburbProfile.java

package com.yosokumo.core.test;

/*
Data set obtained from http://archive.ics.uci.edu/ml/datasets/Housing
*/

public class SuburbProfile
{
    double  crimeRate;      // per capita crime rate by town
    double  zonedBig;       // proportion of residential land zoned for lots 
                            //     over 25,000 sq.ft.
    double  industrialArea; // proportion of non-retail business acres per town
    int     boundsRiver;    // tract bounds river (1 == yes, 0 == no)
    double  noxCon;         // nitric oxides concentration (pp10m)
    double  numRooms;       // average number of rooms per dwelling
    double  houseAge;       // proportion owner-occupied units built < 1940
    double  distWork;       // weighted distances to 5 employment centers
    int     accessHwy;      // index of accessibility to radial highways      
    double  taxRate;        // full-value property-tax rate per $10,000
    double  pupilTeacher;   // pupil-teacher ratio by town
    double  blackPop;       // 1000(b-0.63)^2 where b = propor. blacks by town
    double  lowerStat;      // % lower status of the population
    double  medHomeValue;   // median value of owner-occupied homes in $1000's

}   //  end class SuburbProfile

// end SuburbProfile.java

