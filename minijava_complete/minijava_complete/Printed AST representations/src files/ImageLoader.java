// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import java.net.URI;
// ... other imports
// import javax.imageio.ImageIO;

class TestImage {
	public static void main(String[] args) {
      if(Boundary.test((new ImageLoader()).loadBufferedImage(Boundary.toBinary(args[0])))) {
      	System.out.println(Boundary.results());
      } else System.out.println(Boundary.failure());
	}
}

/**
 * Wrapper class implemented to load buffered images.
 * @author Oleh Godunok
 */
class ImageLoader {

	/**
	 * Loads image stored at a given pathname.
	 * @requires pathName != null && pathName correctly formed 
	 * @param pathName path including name of BufferedImage to load
	 * @return BufferedImage found at pathName
	 */
	
	public BufferedImage loadBufferedImage(BinaryString pathName){
		BufferedImage image;
		File f;
		URI uri;
		// returns a buffered image that can be directly manipulated
		f = new File(); 
		f = f.open(Boundary.toString(pathname));
		uri = f.toURI(); //to URI Uniform Resource Identifier 
	    image = ImageIO.read(uri.toURL()); //Complete URL path as argument
		return image;
	}
}
