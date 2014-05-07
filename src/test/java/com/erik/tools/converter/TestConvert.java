package com.erik.tools.converter;

/**
 * Write class comments here.
 * User: caiwd
 * Date: 14-5-7 上午11:01
 * version $Id: TestConvert.java, v0.1 Exp $.
 */
public class TestConvert {

    @org.junit.Test
    public void testConvertObject() throws Exception {

        TestBean testBean = new TestBean();
        testBean.setName("李雷");
        testBean.setAge(19);
        testBean.setLike("苹果");

        TestBeanTarget testBeanTarget = BeanConverter.convert(new TestBeanTarget(), testBean);

        System.out.println(testBeanTarget.getName());
        System.out.println(testBeanTarget.getAge());
        System.out.println(testBeanTarget.getLike());


    }
}
