# ZHttpExamples
```scala
sealed trait Schema[A] {
  self =>

  type Accessors[Lens[_, _, _], Prism[_, _, _], Traversal[_, _]]

  def ? : Schema[Option[A]]

  def <*>[B](that: Schema[B]): Schema[(A, B)]

  def <+>[B](that: Schema[B]): Schema[scala.util.Either[A, B]]

  def defaultValue: scala.util.Either[String, A]

  def annotations: Chunk[Any]

  def ast: MetaSchema 

  def annotate(annotation: Any): Schema[A]

  def coerce[B](newSchema: Schema[B]): Either[String, Schema[B]]

  def diff(thisValue: A, thatValue: A): Patch[A]

  def patch(oldValue: A, diff: Patch[A]): scala.util.Either[String, A]

  def fromDynamic(value: DynamicValue): scala.util.Either[String, A] 

  def makeAccessors(b: AccessorBuilder): Accessors[b.Lens, b.Prism, b.Traversal]

  def migrate[B](newSchema: Schema[B]): Either[String, A => scala.util.Either[String, B]]

  def optional: Schema[Option[A]]

  def ordering: Ordering[A]

  def orElseEither[B](that: Schema[B]): Schema[scala.util.Either[A, B]]

  def repeated: Schema[Chunk[A]]

  def serializable: Schema[Schema[A]]

  def toDynamic(value: A): DynamicValue

  def transform[B](f: A => B, g: B => A): Schema[B]
  
  def transformOrFail[B](f: A => scala.util.Either[String, B], g: B => scala.util.Either[String, A]): Schema[B]

  def validate(value: A)(implicit schema: Schema[A]): Chunk[ValidationError]

  def zip[B](that: Schema[B]): Schema[(A, B)]
}
```

A SAM (Single Abstract Method) type refers to a functional interface or trait that has only one abstract method.
In Scala, traits with a single abstract method can be used in contexts that expect function types. This allows instances of classes or functions that implement or extend such traits to be used interchangeably with functions.
For example, in Scala, if you have a trait like `JsonEncoder[A]` with a single abstract method `unsafeEncode`:
```scala
trait JsonEncoder[A] {
  def unsafeEncode(a: A, indent: Option[Int], out: Write): Unit
}

```

You can provide implementations of this trait using anonymous classes or lambda expressions. Here's how you might define an instance of `JsonEncoder[String]` using a lambda expression:

```scala
val stringEncoder: JsonEncoder[String] = (a: String, indent: Option[Int], out: Write) =>
  out.write(a)

```
In this example, the lambda expression (a: String, indent: Option[Int], out: Write) => out.write(a) implements the single abstract method unsafeEncode of the JsonEncoder[String] trait. This lambda expression can be used wherever a JsonEncoder[String] is expected


In ZIO, a FiberId represents a unique identifier for a fiber. When you start a fiber using ZIO#fork, you receive a Fiber value, which represents the started computation. This Fiber contains a FiberId, which can be used to identify and interact with the fiber

You can think of a FiberId as a handle that allows you to perform operations on a specific fiber, such as interrupting it, joining it, or querying its status.

Here's a brief overview of what you can do with a FiberId in ZIO:

- Interrupting a Fiber: You can interrupt a fiber using its FiberId. This allows you to cancel a running computation gracefully.

- Joining a Fiber: You can join a fiber using its FiberId, which allows you to wait for the fiber to complete and retrieve its result or error.

- Querying Fiber Status: You can query the status of a fiber (e.g., whether it's running, succeeded, failed, or interrupted) using its FiberId.

- Combining Fibers: You can combine multiple fibers using various operators provided by ZIO, such as zip, zipPar, race, etc., to perform concurrent and parallel computations


[Schema](https://zio.dev/zio-schema/operations/transforming-schemas)