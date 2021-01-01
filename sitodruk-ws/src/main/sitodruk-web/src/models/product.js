export default class Product {
    constructor(id,name, categoryName,price,dtoVersion,quantity,stock) {
      this.id = id;
      this.name = name;
      this.categoryName = categoryName;
      this.price = price;
      this.dtoVersion = dtoVersion;
      this.quantity = quantity,
      this.stock = stock
    }
  }