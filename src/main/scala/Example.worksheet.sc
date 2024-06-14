import zio.schema._

case class Person(name: String, age: Int)

object Person {

  implicit val schema: Schema.CaseClass2.WithFields["name", "age", String, Int, Person] =
    DeriveSchema.gen[Person]

}

val person = Person("John Doe", 42)
Person.schema.field1.get(person)
Person.schema.field2.get(person)
Person.schema.validate(person)
Person.schema.construct("hello", 23)
val dynamicPerson = DynamicValue.fromSchemaAndValue(Person.schema, person)
// or we can call `toDynamic` on the schema directly:
// val dynamicPerson = Person.schema.toDynamic(person)
println(dynamicPerson)

import zio.schema._
import zio.schema.codec._
import zio.Chunk

object AutomaticSchemaDerivation {

  final case class Person(name: String, age: Int)

  object Person {
    implicit val schema: Schema[Person] = DeriveSchema.gen[Person]
  }

  sealed trait PaymentMethod

  final case class CreditCard(
    number: String,
    expirationMonth: Int,
    expirationYear: Int
  ) extends PaymentMethod

  final case class WireTransfer(accountNumber: String, bankCode: String) extends PaymentMethod

  final case class Customer(person: Person, paymentMethod: PaymentMethod)

  implicit val schema: Schema[PaymentMethod] =
    DeriveSchema.gen[PaymentMethod]

  object Customer {
    implicit val schema: Schema[Customer] = DeriveSchema.gen[Customer]
  }

  // Create a customer instance
  val customer =
    Customer(
      person = Person("John Doe", 42),
      paymentMethod = CreditCard("1000100010001000", 6, 2024)
    )

  // Create binary codec from customer
  val customerCodec: BinaryCodec[Customer] =
    ProtobufCodec.protobufCodec[Customer]

  // Encode the customer object
  val encodedCustomer: Chunk[Byte] = customerCodec.encode(customer)

  // Decode the byte array back to the person instance
  val decodedCustomer: Either[DecodeError, Customer] =
    customerCodec.decode(encodedCustomer)

  assert(Right(customer) == decodedCustomer)
  val customerJsonCodec = JsonCodec.jsonCodec(Customer.schema)

  val customerJson = customerJsonCodec.encoder.encodeJson(customer)

  println(customerJson)

}

trait Hello {
  def sayHi(k: Int): Int
}

val f: Hello = (d: Int) => d
