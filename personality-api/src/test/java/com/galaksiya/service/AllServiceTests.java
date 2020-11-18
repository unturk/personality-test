package com.galaksiya.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AnswerServiceTest.class, AnswerGroupServiceTest.class, CategoryServiceTest.class,
	QuestionServiceTest.class})
public class AllServiceTests {
}
