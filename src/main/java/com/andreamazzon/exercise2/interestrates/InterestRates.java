package com.andreamazzon.exercise2.interestrates;

/**
 * This class includes simple examples about computation with interest rates.
 *
 * @author Andrea Mazzon
 */
public class InterestRates {

	/**
	 * It computes the daily interest rate at which an amount of money has to be
	 * invested for 365 days in order to get a given annual interest rate.
	 * 
	 * @param annualRate, the annual interest rate
	 * @return the daily rate
	 */
	public static double computeDailyRate(double annualRate) {
		return Math.pow(1.0 + annualRate, 1 /* try to set 1 instead of 1.0: what does it happen? */ / 365.0) - 1;
	}

	/**
	 * It computes the amount of money that an investor would receive when investing
	 * a given initial amount of money for a given number of days, at a given daily
	 * interest rate.
	 * 
	 * @param initialMoney, initial amount of money in the bank account
	 * @param dailyRate,    daily interest rate
	 * @param numberOfDays, number of days
	 * @return the final amount of money
	 */
	public static double computeBankAccount(double initialMoney, double dailyRate, int numberOfDays) {
		double finalWealth = initialMoney; // everything accrued daily
		for (int i = 1; i <= numberOfDays; i++) {
			finalWealth *= (1 + dailyRate); // finalWealth = finalWealth*(1+rate)
		}
		// finalWealth= initialMoney* Math.pow((1+dailyRate).numberOfDays);
		return finalWealth;
	}

	/**
	 * It computes the amount of money that an investor would receive when investing
	 * a given initial amount of money for a given number of days, at a given daily
	 * interest rate, if the bank, at the end of each day, rounds it down to the
	 * nearest eurocent.
	 * 
	 * @param initialMoney, initial amount of money in the bank account
	 * @param dailyRate,    daily interest rate
	 * @param numberOfDays, number of days
	 * @return the final amount of money
	 */
	public static double computeBankAccountRounded(double initialMoney, double dailyRate, int numberOfDays) {
		double finalWealth = initialMoney * 100; // amount in eurocents
		for (int i = 1; i <= numberOfDays; i++) {
			finalWealth = Math.floor(finalWealth * (1 + dailyRate)); // flooring
		}
		return finalWealth / 100;// amount converted back in euros
	}

	public static void main(String[] args) {
		double annualRate = 0.027;

		double dailyRate = computeDailyRate(annualRate); // daily rate given the annual rate

		double arithmeticAverageRate = annualRate / 365;

		System.out.println("The daily rate is " + dailyRate
				+ " while the daily arithmetic average of the annual rate is  " + arithmeticAverageRate);

		System.out.println("with a relative difference of: " + Math.abs(dailyRate - arithmeticAverageRate) / dailyRate);

		System.out.println();

		int holdingPeriod = 18 * 30; // 18 months, 30 days per month
		double initialWealth = 2700;

		System.out.println("An amount of " + initialWealth + " euros is invested at the beginning");

		double money = computeBankAccount(initialWealth, dailyRate, holdingPeriod);
		System.out.println("After " + holdingPeriod + " days the total in the bank account is " + money);

		System.out.println();

		double moneyRounded = computeBankAccountRounded(initialWealth, dailyRate, holdingPeriod);
		System.out.println("By rounding to the lowest eurocent the bank account is reduced to " + moneyRounded);

		double absoluteDifference = money - moneyRounded;
		double relativeDifferencePercentage = (money - moneyRounded) / moneyRounded * 100;
		System.out.println(
				" with a difference of " + absoluteDifference + " euros in absolute terms which corresponds to "
						+ relativeDifferencePercentage + "% in percentage terms.");
	}
}
