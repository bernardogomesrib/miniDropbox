import * as jspb from 'google-protobuf'



export class VehicleStatus extends jspb.Message {
  getVehicleId(): string;
  setVehicleId(value: string): VehicleStatus;

  getX(): number;
  setX(value: number): VehicleStatus;

  getY(): number;
  setY(value: number): VehicleStatus;

  getStatus(): string;
  setStatus(value: string): VehicleStatus;

  getSpeed(): number;
  setSpeed(value: number): VehicleStatus;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): VehicleStatus.AsObject;
  static toObject(includeInstance: boolean, msg: VehicleStatus): VehicleStatus.AsObject;
  static serializeBinaryToWriter(message: VehicleStatus, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): VehicleStatus;
  static deserializeBinaryFromReader(message: VehicleStatus, reader: jspb.BinaryReader): VehicleStatus;
}

export namespace VehicleStatus {
  export type AsObject = {
    vehicleId: string,
    x: number,
    y: number,
    status: string,
    speed: number,
  }
}

export class CentralCommand extends jspb.Message {
  getCommand(): string;
  setCommand(value: string): CentralCommand;

  getMessage(): string;
  setMessage(value: string): CentralCommand;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): CentralCommand.AsObject;
  static toObject(includeInstance: boolean, msg: CentralCommand): CentralCommand.AsObject;
  static serializeBinaryToWriter(message: CentralCommand, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): CentralCommand;
  static deserializeBinaryFromReader(message: CentralCommand, reader: jspb.BinaryReader): CentralCommand;
}

export namespace CentralCommand {
  export type AsObject = {
    command: string,
    message: string,
  }
}

export class EstimateRequest extends jspb.Message {
  getVehicleId(): string;
  setVehicleId(value: string): EstimateRequest;

  getDestination(): string;
  setDestination(value: string): EstimateRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): EstimateRequest.AsObject;
  static toObject(includeInstance: boolean, msg: EstimateRequest): EstimateRequest.AsObject;
  static serializeBinaryToWriter(message: EstimateRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): EstimateRequest;
  static deserializeBinaryFromReader(message: EstimateRequest, reader: jspb.BinaryReader): EstimateRequest;
}

export namespace EstimateRequest {
  export type AsObject = {
    vehicleId: string,
    destination: string,
  }
}

export class EstimateResponse extends jspb.Message {
  getEstimate(): string;
  setEstimate(value: string): EstimateResponse;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): EstimateResponse.AsObject;
  static toObject(includeInstance: boolean, msg: EstimateResponse): EstimateResponse.AsObject;
  static serializeBinaryToWriter(message: EstimateResponse, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): EstimateResponse;
  static deserializeBinaryFromReader(message: EstimateResponse, reader: jspb.BinaryReader): EstimateResponse;
}

export namespace EstimateResponse {
  export type AsObject = {
    estimate: string,
  }
}

