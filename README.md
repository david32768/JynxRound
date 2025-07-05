# Jynx
[Jynx(bird)](https://en.wikipedia.org/wiki/Wryneck)

## JynxRound

```
 {JynxFree} roundtrip {options}  class-name|class_file
   (checks that TOJYNX followed by JYNX produces an equivalent class (according to ASM Textifier))
```

Options are:

```
 --USE_STACK_MAP use supplied stack map instead of ASM generated
 --BASIC_VERIFIER use ASM BasicVerifier instead of ASM SimpleVerifier
 --ALLOW_CLASS_FORNAME let simple verifier use Class.forName() for non-java classes
 --USE_CLASSFILE use java.lang.classfile
 --SKIP_FRAMES do not produce stack map
 --DOWN_CAST if necessary reduces JVM release to maximum supported by ASM version
 --DEBUG print stack trace(s)
 --SUPPRESS_WARNINGS suppress warnings

```

