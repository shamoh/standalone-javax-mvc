# About

This project demonstrates how to create **standalone Java EE MVC application**.

[MVC](https://mvc-spec.java.net/) is newly developed Java EE
[JSR 371: Model-View-Controller (MVC 1.0) Specification](https://jcp.org/en/jsr/detail?id=371).

> The MVC API defines an action-oriented Web framework as an alternative to the component-oriented JSF.
> In an action-oriented framework, developers are responsible for all the controller logic and are given 
> full control of the URI space for their application.
>
> The MVC API is layered on top of [JAX-RS](https://jax-rs-spec.java.net/) over [Servlets](https://jcp.org/en/jsr/detail?id=315)
> and integrates with existing EE technologies like [CDI](https://www.jcp.org/en/jsr/detail?id=365)
> and [Bean Validation](https://jcp.org/en/jsr/detail?id=349).
  
It uses following technologies:

- [Ozark](https://ozark.java.net/) as reference implementation of `MVC` specification
- [Mustache](https://mustache.github.io/) as MVC template engine
- [Undertow](http://undertow.io/) as implementation of `Servlets` specification
- [Weld](http://weld.cdi-spec.org/) as reference implementation of `CDI` specification
- [DeltaSpike](https://deltaspike.apache.org/) as extension library of `CDI`
- [Jersey](https://jersey.java.net/) as reference implementation of `JAX-RS` specification

See my [blog post](http://yatel.kramolis.cz/2016/03/how-to-run-standalone-java-ee-mvc.html) for more details.

# Build it

```
gradlew build
```

# Run it

```
gradlew run
```

# Try it

```
curl -vvv http://localhost:8080/resources/hello?user=Libor
```

# Distribute it

```
gradlew distZip
```

And `build/distributions/` contains distribution zip.
