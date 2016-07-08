# http request body verifier

## 目标场景

RESTful api如果采用JSON承载数据的话, 经常性的POST这样的数据: 
```
{
  "foo": "hello,world",
  "foo3": 100,
  "foo8": { "question": "who am i?" }
}
```
为了安全，后端也需要对提交的数据进行有效性验证。

## 先来个例子
```
IResultCallback callback = ...;
Map<String, Object> target = ...;

boolean result = new Verifier()
  .result(callback)   //这是可选的
  //rule方法有5个重载
  .rule("foo")   //必须存在foo属性
  .rule("foo2", "[0-9]{1,3}") //foo2属性必须存在, 并且值必须是1到3位的数字字符串
  .rule("foo3", true, "[0-9]{1,3}") //foo3属性可以不存在, 如果有的话, 其值必须是1到3位的数字字符串
  .rule("foo4", new IChecker() {
    public boolean check(Object value) { ... }
  })    //通过实现IChecker对foo4值进行检查
  .rule("foo5", true, new IChecker() {
    public boolean check(Object value) { ... }
  })    //通过实现IChecker对foo5值进行检查, foo5可以不存在
  .verify(target);
```

上述例子是一般用法, 如果设置了```IResultCallback```的话, 除了返回结果```result```以外, ```IResultCallback```的方法```void result(boolean success, String reason);```中, 也可以获得校验结果和原因(第二个参数```reason```). 如果是java8+可以用lambda很方便的进行调用

## 检查器 IChecker

目前已经实现的检查器有3个:

* AlwaysTrueChecker
* AlwaysFalseChecker
* RegexChecker 通过正则表达式进行检查

如果这些检查器不够用, 可以像例子中的foo4/foo5一样自己实现新的检查器