package com.wavesplatform.lang.v1

import com.wavesplatform.lang.Common._
import com.wavesplatform.lang.ScriptVersion.Versions.V1
import com.wavesplatform.lang.v1.compiler.Terms._
import com.wavesplatform.lang.v1.testing.ScriptGen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, PropSpec}

class DenyDuplicateVarNamesTest extends PropSpec with PropertyChecks with Matchers with ScriptGen with NoShrink {

  val test = DenyDuplicateVarNames(V1, Set("height"), _: EXPR)

  property("allow $ duplicates")(
    test(BLOCK(LET("$x", CONST_BOOLEAN(true)), BLOCK(LET("$x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true)))) shouldBe 'right)

  property("deny overwrite height")(
    DenyDuplicateVarNames(V1, Set("height"), BLOCK(LET("height", CONST_BOOLEAN(true)), CONST_BOOLEAN(true))) should produce("height"))

  property("deny duplicates in block")(
    test(BLOCK(LET("x", CONST_BOOLEAN(true)), BLOCK(LET("x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true)))) should produce("x"))

  property("deny @ args")(test(BLOCK(LET("@a", CONST_BOOLEAN(true)), CONST_BOOLEAN(true))) should produce("@"))

  property("deny duplicates in if cond")(
    test(IF(BLOCK(LET("x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true)),
            BLOCK(LET("x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true)),
            CONST_BOOLEAN(true))) should produce("x"))

  property("deny duplicates in if branch")(
    test(IF(CONST_BOOLEAN(true),
            BLOCK(LET("x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true)),
            BLOCK(LET("x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true)))) should produce("x"))

  property("deny duplicates in funcitoncall")(
    test(
      FUNCTION_CALL(
        FunctionHeader.User("foo"),
        List(BLOCK(LET("x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true)), BLOCK(LET("x", CONST_BOOLEAN(true)), CONST_BOOLEAN(true))))) should produce(
      "x"))

}
