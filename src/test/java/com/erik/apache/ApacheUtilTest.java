package com.erik.apache;

import com.erik.tools.converter.TestBean;
import com.erik.tools.converter.TestBeanTarget;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

/**
 * Write class comments here.
 * User: caiwd
 * Date: 14-5-7 下午3:07
 * version $Id: util.java, v0.1 Exp $.
 */
public class ApacheUtilTest {

    @Test
    public void testCopyProperties() throws Exception {

        TestBean testBean = new TestBean();
        testBean.setName("李雷");
        testBean.setAge(19);
        testBean.setLike("苹果");

        TestBeanTarget testBeanTarget = new TestBeanTarget();

        BeanUtils.copyProperties(testBeanTarget,testBean);

        System.out.println(testBeanTarget.getName());
        System.out.println(testBeanTarget.getAge());
        System.out.println(testBeanTarget.getLike());


    }
}
