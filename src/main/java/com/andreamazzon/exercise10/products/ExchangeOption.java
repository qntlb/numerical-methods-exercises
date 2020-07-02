package com.andreamazzon.exercise10.products;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.stochastic.RandomVariable;

/**
 * This class represents an exchange option, involving therefore a two-dimensional process
 *
 * @author Andrea Mazzon
 *
 */
public class ExchangeOption extends AbstractAssetMonteCarloProduct {

        private final double maturity;
        private final int firstAssetIndex;
        private final int secondAssetIndex;
        /**
         * Construct a product representing an Exchange option on two assets S^1, S^2:
         * S^1 is the asset with index firstAssetIndex from the model and S^2
         * with index secondAssetIndex
         *
         * @param underlyingName Name of the underlying
         * @param maturity       The maturity T in the option payoff max(S(T)-K,0)
         */
        public ExchangeOption(double maturity, int firstAssetIndex, int secondAssetIndex) {
                this.maturity = maturity;
                this.firstAssetIndex= firstAssetIndex;
                this.secondAssetIndex = secondAssetIndex;
        }
        //overloaded constructor:  firstAssetIndex = 0, secondAssetIndex = 1
        public ExchangeOption(double maturity) {
                this(maturity,0,1);//note this use of this
        }

        @Override
        public RandomVariable getValue(double evaluationTime,
                        AssetModelMonteCarloSimulationModel model)
                                        throws CalculationException {
                // Get S^1(T), S^2(T)
                final RandomVariable firstAssetAtMaturity = model.getAssetValue(maturity,
                                firstAssetIndex);
                final RandomVariable secondAssetAtMaturity = model.getAssetValue(maturity,
                                secondAssetIndex);

                // Payoff of the exchange option
                RandomVariable values = firstAssetAtMaturity.sub(secondAssetAtMaturity).
                                floor(0.0);

                // Discounting...
                final RandomVariable numeraireAtMaturity = model.getNumeraire(maturity);
                values = values.div(numeraireAtMaturity);

                // ...to evaluation time.
                final RandomVariable numeraireAtEvalTime = model.getNumeraire(evaluationTime);
                values = values.mult(numeraireAtEvalTime);

                return values;
        }

}