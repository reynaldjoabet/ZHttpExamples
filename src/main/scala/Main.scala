import zio.schema._
import zio.Chunk

object Main extends App {

  println("Hello, World!")

  import zio.http.handler
  import zio.http.Cookie
  import zio.http.Decompression
  import zio.http.DnsResolver
  import zio.http.Handler
  import zio.http.HttpApp
  import zio.http.Middleware.bearerAuth
  import zio.http.Middleware.cors
  import zio.http.Middleware.signCookies
  import zio.http.Middleware.timeout
  import zio.http.RequestHandler
  import zio.http.Routes
  import zio.schema.codec.json
  import zio.schema.codec.BinaryCodecs
  import zio.schema.codec.Codec
  import zio.schema.codec.Codecs
  import zio.schema.codec.Decoder
  import zio.schema.codec.Encoder
  import zio.schema.codec.JsonCodec
  import zio.schema.codec.ProtobufCodec

//So we can easily create a Schema for a primitive type A either by calling Schema.primitive[A] or by calling Schema.apply[A]:

  val intSchema1: Schema[Int] = Schema[Int]
  val intSchema2: Schema[Int] = Schema.primitive[Int]

  case class Person(name: String, age: Int)

  object Person {
    // implicit val schema: Schema[Person] = DeriveSchema.gen[Person]

    implicit val schema4: Schema[Person] =
      Schema.CaseClass2[String, Int, Person](
        id0 = TypeId.fromTypeName("Person"),
        field01 = Schema.Field(
          name0 = "name",
          schema0 = Schema[String],
          get0 = _.name,
          set0 = (p, x) => p.copy(name = x)
        ),
        field02 = Schema.Field(
          name0 = "age",
          schema0 = Schema[Int],
          get0 = _.age,
          set0 = (person, age) => person.copy(age = age)
        ),
        construct0 = (name, age) => Person(name, age)
      )

    implicit val listSchema: Schema[List[Person]]     = Schema.list[Person]
    implicit val chunkSchema: Schema[Chunk[Person]]   = Schema.chunk[Person]
    implicit val vectorSchema: Schema[Vector[Person]] = Schema.vector[Person]

    implicit val mapSchema: Schema[scala.collection.immutable.Map[String, Person]] =
      Schema.map[String, Person]

  }

  val personListSchema: Schema[List[Person]] =
    Schema.Sequence[List[Person], Person, String](
      elementSchema = Schema[Person],
      fromChunk = _.toList,
      toChunk = i => Chunk.fromIterable(i),
      annotations = Chunk.empty,
      identity = "List"
    )

  sealed trait PaymentMethod

  object PaymentMethod {

    final case class CreditCard(
      number: String,
      expirationMonth: Int,
      expirationYear: Int
    ) extends PaymentMethod

    final case class WireTransfer(accountNumber: String, bankCode: String) extends PaymentMethod

  }
  // object Schema {
  // sealed trait Enum[Z] extends Schema[Z]
//}

  // case class User(name: String, age: Int, address: Address, friends: List[User])
}
