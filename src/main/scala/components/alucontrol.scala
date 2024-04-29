// This file contains ALU control logic.

package dinocpu.components

import chisel3._
import chisel3.util._

/**
 * The ALU control unit
 *
 * Input:  aluop        Specifying the type of instruction using ALU
 *                          . 0 for none of the below
 *                          . 1 for 64-bit R-type
 *                          . 2 for 64-bit I-type
 *                          . 3 for 32-bit R-type
 *                          . 4 for 32-bit I-type
 *                          . 5 for non-arithmetic instruction types that uses ALU (auipc/jal/jarl/Load/Store)
 * Input:  funct7       The most significant bits of the instruction.
 * Input:  funct3       The middle three bits of the instruction (12-14).
 *
 * Output: operation    What we want the ALU to do.
 *
 * For more information, see Section 4.4 and A.5 of Patterson and Hennessy.
 * This is loosely based on figure 4.12
 */
class ALUControl extends Module {
  val io = IO(new Bundle {
    val aluop     = Input(UInt(3.W))
    val funct7    = Input(UInt(7.W))
    val funct3    = Input(UInt(3.W))

    val operation = Output(UInt(5.W))
  })

  io.operation := "b11111".U // Invalid

  //Your code goes here
  switch(io.aluop){
    is(0.U) { // none of the other types
      return
    }
    is(1.U) { // 64-bit R-type
      switch(io.funct7){
        is("b0000000".U){
          switch(io.funct3){
            is("b000".U) {io.operation := "b00001".U} // add
            is("b001".U) {io.operation := "b10010".U} // sll
            is("b010".U) {io.operation := "b10110".U} // slt
            is("b011".U) {io.operation := "b10111".U} // sltu
            is("b100".U) {io.operation := "b01111".U} // xor
            is("b101".U) {io.operation := "b10100".U} // srl
            is("b110".U) {io.operation := "b01110".U} // or
            is("b111".U) {io.operation := "b01101".U} // and
          }
        }
        is("b0100000".U){
          switch(io.funct3){
            is("b000".U) {io.operation := "b00100".U} // sub
            is("b101".U) {io.operation := "b10000".U} // sra
          }
        }
        is("b0000001".U){
          is("b000".U) {io.operation := "b00110".U} // mul
          is("b001".U) {io.operation := "b00111".U} // mulh
          is("b010".U) {io.operation := "b11000".U} // mulhsu
          is("b011".U) {io.operation := "b01000".U} // mulhu
          is("b100".U) {io.operation := "b01011".U} // div
          is("b101".U) {io.operation := "b01010".U} // divu
          is("b110".U) {io.operation := "b11100".U} // rem
          is("b111".U) {io.operation := "b11011".U} // remu
        }
      }
    }
    is(2.U) { // 64-bit I-type
      return // TODO couldnt find?
    }
    is(3.U) { // 32-bit R-type
      switch(io.funct7){
        is("b0000000".U){
          switch(io.funct3){
            is("b000".U) {io.operation := "b00000".U} // addw
            is("b001".U) {io.operation := "b10011".U} // sllw
            is("b101".U) {io.operation := "b10101".U} // slrw
          }
        }
        is("b0100000".U){
          switch(io.funct3){
            is("b000".U) {io.operation := "b00010".U} // subw
            is("b101".U) {io.operation := "b10001".U} // sraw
          }
        }
        is("b0000001".U){
          is("b000".U) {io.operation := "b00101".U} // mulw
          is("b100".U) {io.operation := "b01001".U} // divw
          is("b101".U) {io.operation := "b01100".U} // divuw
          is("b110".U) {io.operation := "b11010".U} // remw
          is("b111".U) {io.operation := "b11001".U} // remuw
        }
      }
    }
    }
    is(4.U) { // 32-bit I-type
      return // TODO couldnt find?
    }
    is(5.U) { // other (auipc, jal, jarl, load, store)
      return // TODO couldnt find?
    }
  }
  
}
