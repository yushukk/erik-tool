属性拷贝工具
=====================

eg:

TestBean testBean = new TestBean();
testBean.setName("李雷");
testBean.setAge(19);
testBean.setLike("苹果");

TestBeanTarget testBeanTarget = BeanConverter.convert(new TestBeanTarget(), testBean);

System.out.println(testBeanTarget.getName());
System.out.println(testBeanTarget.getAge());
System.out.println(testBeanTarget.getLike());