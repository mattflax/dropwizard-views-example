# DropWizard external views - example project

This project illustrates how to add external templates to a DropWizard
application, allowing you, for example, to have dynamic templates for
an evolving set of document types.

It came about after we revisited a client who needed to do exactly that. They
were using DropWizard 0.6.2, and it was unexpectedly difficult to make
changes to templates, or to add new templates to the project. I decided to
investigate how difficult it would be to do the same thing in a more recent
version of DropWizard - 0.9.2.


## Updated and extended classes

- `MultiLocationViewBundle` - this is a drop-in replacement for the default
DropWizard `ViewBundle`. It relies on...
- `MultiLocationViewConfiguration` - this extends the default DropWizard
`Configuration` class, adding a `views` element with details of the template
locations.
- `ViewsConfiguration` - a configuration class representing the content of the
`views` element held in the `MultiLocationViewConfiguration`.
- `MultiLocationViewRenderer` - an extension to the DropWizard `ViewRenderer`
interface, adding a `configureLocations()` method.
- multiple location variants of the view renderers for both Freemarker and
Mustache templates. There is quite a lot of duplicated code here, 
unfortunately.


## Configuration

The view configuration should look like that given in the 
[config/example.yml](config/example.yml) file:

```
# Additional configuration for the external views.
views:

  templatePaths:
    - /data/templates/freemarker
    - /data/templates/mustache
    - classpath

  renderers:
    .ftl:
      # Freemarker configuration
```

The `templatePaths` element holds an array of paths to be used to search
for templates. Note that the special `classpath` reference, which indicates
that the jar file should be included in the list of search locations, is
**not** assumed or implied, and needs to be explicitly specified. If
`templatePaths` is not given, the jar file will be used as normal.

The `renderers` element contains the renderer-specific configuration, and
will be passed to the appropriate Renderer implementation as mentioned
in the [DropWizard Views documentation](http://www.dropwizard.io/0.9.2/docs/manual/views.html).