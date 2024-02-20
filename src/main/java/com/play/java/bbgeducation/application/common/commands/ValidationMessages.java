package com.play.java.bbgeducation.application.common.commands;

public final class ValidationMessages {

    public static String EmptyName(String entityName){
        return capitalizeFirstLetter(entityName.toLowerCase()) + " name cannot be empty.";
    }

    public static String NameLength(String entityName){
        return NameLength(entityName, ValidationLengths.NameMin(), ValidationLengths.NameMax());
    }
    public static String NameLength(String entityName, Integer min, Integer max){
        if (min != null && max != null) {
            return String.format("%s must be between %s and %s characters.", capitalizeFirstLetter(entityName.toLowerCase()), min, max);
        }
        if (min != null){
            return String.format("%s must be greater than  %s characters.", capitalizeFirstLetter(entityName.toLowerCase()), min);
        }
        if (max != null){
            return String.format("%s must be less than  %s characters.", capitalizeFirstLetter(entityName.toLowerCase()), max);
        }
        return String.format("%s length is invalid.", capitalizeFirstLetter(entityName.toLowerCase()));
    }

    public static String DescriptionLength(String entityName){
        return DescriptionLength(entityName, ValidationLengths.DescriptionMax());
    }
    public static String DescriptionLength(String entityName, int max){
        return String.format("%s must be less than  %s characters.", capitalizeFirstLetter(entityName.toLowerCase()), max);
    }

    public static String EndAfterStartDate(String entityName){
        return String.format("$s end date must be after the start date.", capitalizeFirstLetter(entityName.toLowerCase()));
    }

    private static String capitalizeFirstLetter(String text){
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }
}
