import zio.schema._
import zio.schema.codec._
import zio.Chunk

object AutomaticSchemaDerivation extends App {

  final case class Person(name: String, age: Int)

  object Person {
    implicit val schema: Schema[Person] = DeriveSchema.gen[Person]
  }

  sealed trait PaymentMethod

  object PaymentMethod {

    implicit val schema: Schema[PaymentMethod] =
      DeriveSchema.gen[PaymentMethod]

    final case class CreditCard(
      number: String,
      expirationMonth: Int,
      expirationYear: Int
    ) extends PaymentMethod

    final case class WireTransfer(accountNumber: String, bankCode: String) extends PaymentMethod

  }

  final case class Customer(person: Person, paymentMethod: PaymentMethod)

  object Customer {
    implicit val schema: Schema[Customer] = DeriveSchema.gen[Customer]
  }

  // Create a customer instance
  val customer =
    Customer(
      person = Person("John Doe", 42),
      paymentMethod = PaymentMethod.CreditCard("1000100010001000", 6, 2024)
    )

  // Create binary codec from customer
  val customerCodec: BinaryCodec[Customer] =
    ProtobufCodec.protobufCodec[Customer]

  // Encode the customer object
  val encodedCustomer: Chunk[Byte] = customerCodec.encode(customer)

  println(encodedCustomer)

  // Decode the byte array back to the person instance
  val decodedCustomer: Either[DecodeError, Customer] =
    customerCodec.decode(encodedCustomer)

  println(decodedCustomer)
  assert(Right(customer) == decodedCustomer)
  val customerJsonCodec: zio.json.JsonCodec[Customer] = JsonCodec.jsonCodec(Customer.schema)

  val customerJson = customerJsonCodec.encodeJson(customer, None)

  println(customerJson)

  println(customerJsonCodec.decoder.decodeJson(customerJson))

}
