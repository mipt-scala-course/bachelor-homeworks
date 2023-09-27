package mipt.functors

sealed trait DecoderError

case object NumberFormatDecoderError extends DecoderError

case object IllegalArgumentDecoderError extends DecoderError

case object InvalidDegreesFahrenheitValue extends DecoderError
