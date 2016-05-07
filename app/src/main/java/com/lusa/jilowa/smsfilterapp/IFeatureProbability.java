package com.lusa.jilowa.smsfilterapp;

/**
 * Simple interface defining the method to calculate the feature probability.
 *
 * @author Jilowa Lusa
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 */
public interface IFeatureProbability<T, K> {

    float featureProbability(T feature, K category);

}
