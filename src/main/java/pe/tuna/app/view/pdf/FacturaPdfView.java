package pe.tuna.app.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import pe.tuna.app.models.entity.Factura;
import pe.tuna.app.models.entity.ItemFactura;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura = (Factura) model.get("factura");

        PdfPTable table1 = new PdfPTable(1);
        table1.setSpacingAfter(20);
        
        table1.addCell("Datos del cliente");
        table1.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        table1.addCell(factura.getCliente().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);
        table2.addCell("Datos de la factura");
        table2.addCell("Folio: " + factura.getId());
        table2.addCell("Descripcion: " + factura.getDescripcion());
        table2.addCell("Fecha: " + factura.getCreateAt());

        PdfPTable tableDetalle = new PdfPTable(4);
        tableDetalle.addCell("Producto");
        tableDetalle.addCell("Precio");
        tableDetalle.addCell("Cantidad");
        tableDetalle.addCell("Importe");

        for (ItemFactura item: factura.getItems()) {
            tableDetalle.addCell(item.getProducto().getNombre());
            tableDetalle.addCell(item.getProducto().getPrecio().toString());
            tableDetalle.addCell(item.getCantidad().toString());
            tableDetalle.addCell(item.calcularImporte().toString());
        }

        PdfPCell cell = new PdfPCell(new Phrase("TOTAL: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);

        tableDetalle.addCell(cell);
        tableDetalle.addCell(factura.getTotal().toString());

        document.add(table1);
        document.add(table2);
        document.add(tableDetalle);

    }
}
