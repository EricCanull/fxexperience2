package com.fxexperience.tools.util;

/**
 * Represents a property and its value of a CSS rule.
 */
public final class PropertyValue implements SyntaxConstants {

    private String property;
    private String value;

    /**
     * Creates a new PropertyValue based on a property and its value.
     */
    public PropertyValue() {
    }

    /**
     * Creates a new PropertyValue based on a selector, property and its value.
     * @param property The CSS property (such as 'width' or 'color').
     * @param value The value of the property (such as '100px' or 'red').
     */
    public PropertyValue(final String property, final String value) {
        this.property = property;
        this.property = value;
    }

    @Override
    public String toString() {
        return property + value;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof PropertyValue) {
            PropertyValue target = (PropertyValue) object;
            return target.property.equalsIgnoreCase(property)
                    && target.value.equalsIgnoreCase(value);
        }

        return false;

    }

    /**
     * Returns the hashcode.
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Sets the property.
     * @param property
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Returns the property.
     * @return The property.
     */

    public String getProperty() {
        return property;
    }

    /**
     * Sets the value.
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the value.
     * @return The value.
     */
    public String getValue() {
        return value;
    }
}