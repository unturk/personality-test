package com.galaksiya.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum QuestionType {

	SINGLE_CHOICE(0, "single_choice"),

	CONDITIONAL_SINGLE_CHOICE(1, "single_choice_conditional"),

	NUMBER_RANGE(2, "number_range");

    /**
     * Holds the type of visualization of category.
     */
    private final String questionType;

	/**
	 * Holds the index of visualization of category.
	 */
	private final int index;

    QuestionType(int index, String type) {
    	this.index = index;
        this.questionType = type;
    }

    public String getValue() {
        return questionType;
    }

	public int getIndex() {
		return index;
	}

	/**
	 * Conversion map to retrieve {@link QuestionType} with given int value.
	 */
	private static final Map<Integer, QuestionType> intToTypeMap = new HashMap<>();

	static {
		Arrays.stream(QuestionType.values()).forEach(type -> intToTypeMap.put(type.index, type));
	}

	/**
	 * For given integer value parameter, returns the corresponding {@link QuestionType} that has given value if
	 * found. Otherwise returns <code>null</code>.
	 *
	 * @param value Integer value of the {@link QuestionType#index} field.
	 * @return QuestionType instance or <code>null</code> if not found.
	 */
	public static QuestionType fromValue(int value) {
		return intToTypeMap.get(value);
	}
}
