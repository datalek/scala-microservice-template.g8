import cats.data.ReaderT
import cats.effect.IO

package object rio {

  type RIO[E, R] = ReaderT[IO, E, R]

  def ask[A]: RIO[A, A] =
    ReaderT.ask[IO, A]

  def runRIO[E, R](env: E, in: RIO[E, R]): IO[R] =
    in.run(env)
}
