export default class Order {
    constructor(payUOrderId,products,totalValue,username,blikCode,addressDTO,orderStatus,dtoVersion) {
      this.payUOrderId = payUOrderId;
      this.products = products;
      this.totalValue = totalValue;
      this.username = username;
      this.blikCode = blikCode;
      this.addressDTO = addressDTO;
      this.orderStatus = orderStatus;
      this.dtoVersion = dtoVersion;
    }
  }