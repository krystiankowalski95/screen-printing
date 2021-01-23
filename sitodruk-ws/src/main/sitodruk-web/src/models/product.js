export default class Product {
    constructor(id,name, categoryName,price,dtoVersion,quantity,stock,isActive) {
      this.id = id;
      this.name = name;
      this.price = price;
      this.categoryName = categoryName;
      this.dtoVersion = dtoVersion;
      this.quantity = quantity,
      this.stock = stock,
      this.isActive = isActive;
    }
  }