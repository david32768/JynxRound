# Jynx
[Jynx(bird)](https://en.wikipedia.org/wiki/Wryneck)

## JynxRound

This requires Java V24 and  [ASM](https://asm.ow2.io)  9.8

```
 {JynxFree} roundtrip {options}  class-name|class_file|txt-file
  (checks that TOJYNX followed by JYNX produces an equivalent class)
   (according to ASM Textifier)
   (txt-file is a .txt file containing [ {options} [class-name|class-file] ]*)
```

Options are:

```
 --USE_STACK_MAP use supplied stack map instead of ASM generated
 --BASIC_VERIFIER only use ASM BasicVerifier
 --VERIFIER_PLATFORM Use ClassFile Verifier with Platform Loader only (the default is system loader)
 --SKIP_FRAMES do not produce stack map
 --DOWN_CAST if necessary reduces JVM release to maximum supported by ASM version
 --DEBUG print stack trace(s)
 --SUPPRESS_WARNINGS suppress warnings

```

```
 {JynxFree} compare {options}  class-name|class_file class-name|class_file
   (checks that classes are the same according to ASM Textifier)

```

Options are:

```
--DEBUG print stack trace(s)

```