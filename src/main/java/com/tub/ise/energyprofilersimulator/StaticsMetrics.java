package com.tub.ise.energyprofilersimulator;

public class  StaticsMetrics {

    public static  Integer NUMBER_OF_PIPELINES = 100;  // SIMULATION ITERATION NUMBER- NUMBER OF  PIPELINES/CONSUMERS
    public static  Integer RANDOM_SEED_OF_OPR_PER_PIP = 9;  // THE NUMBER OF OPERATIONS FOR EVERY PIPELINE
    public static  Integer MAX_RANDOM_SEED_OF_OPR_SELECTION = 9; //THIS SHOULD BE SAME TO THE ROW NUMBER OF EXCEL INPUT FILE


//////////////////// ENERGY CONSUMPTION UNITS //////////////////

    public static  Integer PROVIDER_OUTSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION = 1;//3;
    public static  Integer CONSUMER_OUTSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION = 1;//4;
    public static  Integer PROVIDER_INSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION = 1;
    public static  Integer CONSUMER_INSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION = 1;
    public static  Integer PROVIDER_CPU_ENERGY_CONSUMPTION = 1;//100;
    public static  Integer CONSUMER_CPU_ENERGY_CONSUMPTION = 1;//10;
    public static  Integer PROVIDER_MEMORY_ENERGY_CONSUMPTION = 1;//20;
    public static  Integer CONSUMER_MEMORY_ENERGY_CONSUMPTION = 1;//25;
    public static  Integer PROVIDER_DEPLOY_ENERGY_CONSUMPTION = 1;//11;
    public static  Integer CONSUMER_DEPLOY_ENERGY_CONSUMPTION = 1;//9;
    public static  Integer PROVIDER_OBSERVATION_ENERGY_CONSUMPTION = 1;//2;
    public static  Integer CONSUMER_OBSERVATION_ENERGY_CONSUMPTION = 1;//2;
    public static  Integer THRESHOLD_FOR_MIN_COMMON_LIST_LENGTH = 2;//2;

}
