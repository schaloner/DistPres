package controllers;

import forms.PresentationDescriptor;
import models.Image;
import models.Presentation;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.ContentImporter;

import java.io.File;
import java.util.List;

import views.html.index;
import views.html.presentation;

public class Application extends Controller
{
    private enum PresentationIndexOp
    {
        NEXT
                {
                    int calculate(Presentation presentation)
                    {
                        return presentation.currentPosition +
                               ((presentation.currentPosition < presentation.images.size() - 1) ? 1 : 0);
                    }
                },
        PREVIOUS
                {
                    int calculate(Presentation presentation)
                    {
                        return presentation.currentPosition - ((presentation.currentPosition > 0) ? 1 : 0);
                    }
                },
        RESET
                {
                    int calculate(Presentation presentation)
                    {
                        return 0;
                    }
                };

        abstract int calculate(Presentation presentation);
    }

    public static Result index()
    {
        List<Presentation> presentations = Presentation.FIND.all();
        return ok(index.render(presentations));
    }

    public static Result viewPresentation(Long id)
    {
        Presentation pres = Presentation.FIND.byId(id);

        return ok(presentation.render(pres));
    }

    public static Result nextImage()
    {
        return updateIndex(PresentationIndexOp.NEXT);
    }

    public static Result previousImage()
    {
        return updateIndex(PresentationIndexOp.PREVIOUS);
    }

    public static Result resetPresentation()
    {
        return updateIndex(PresentationIndexOp.RESET);
    }

    private static Result updateIndex(PresentationIndexOp op)
    {
        Form<PresentationDescriptor> form = form(PresentationDescriptor.class).bindFromRequest();
        Presentation presentation = Presentation.FIND.byId(form.get().presentationId);
        presentation.currentPosition = op.calculate(presentation);
        presentation.save();

        return ok();
    }

    public static Result poll(Long presentationId)
    {
        Presentation presentation = Presentation.FIND.byId(presentationId);
        return ok("" + presentation.currentPosition);
    }

    public static Result getImageFor(Long presentationId,
                                     Integer imageIndex)
    {
        Presentation presentation = Presentation.FIND.byId(presentationId);
        Image image = presentation.images.get(presentation.currentPosition);
        return ok(image.binaryContent.bytes);
    }

    public static Result upload()
    {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart presentation = body.getFile("presentationFile");
        if (presentation != null)
        {
            String fileName = presentation.getFilename();
            String contentType = presentation.getContentType();
            File file = presentation.getFile();
            boolean ok = new ContentImporter().importContent(fileName,
                                                             contentType,
                                                             file);
            if (!ok)
            {
                flash("error", "Problem uploading presentation");
            }
        }
        else
        {
            flash("error", "Missing file");
        }

        return redirect(routes.Application.index());
    }
}
