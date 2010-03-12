package org.overture.ide.core.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.overture.ide.core.Activator;
import org.overture.ide.core.ICoreConstants;
import org.overture.ide.core.ast.AstManager;
import org.overture.ide.core.ast.IAstManager;
import org.overture.ide.core.ast.IVdmElement;
import org.overture.ide.core.utility.FileUtility;
import org.overture.ide.core.utility.IVdmProject;
import org.overture.ide.core.utility.VdmProject;
import org.overturetool.vdmj.messages.VDMError;
import org.overturetool.vdmj.messages.VDMWarning;

public abstract class AbstractParserParticipant implements ISourceParser
{

	protected String natureId;
	protected IVdmProject project;

	public void setNatureId(String natureId)
	{
		this.natureId = natureId;
	}

	public void setProject(IVdmProject project)
	{
		this.project = project;
	}

	/**
	 * Parses a file and updates file markers and the AstManager with the result
	 */
	public void parse(IFile file)
	{

		ParseResult result;
		try
		{
			result = startParse(file,
					new String(FileUtility.getCharContent(FileUtility.getContent(file))),
					file.getCharset());
			setFileMarkers(file, result);
			if (result != null)
				setParseAst(file.getLocation().toFile().getAbsolutePath(),
						result.getAst(),
						!result.hasParseErrors());

		} catch (CoreException e)
		{
			if (Activator.DEBUG)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Parses a file and updates file markers and the AstManager with the result
	 */
	public void parse(IFile file, String data)
	{

		ParseResult result;
		try
		{
			result = startParse(file, data, file.getCharset());
			setFileMarkers(file, result);
			if (result != null)

				setParseAst(file.getLocation().toFile().getAbsolutePath(),
						result.getAst(),
						!result.hasParseErrors());
		} catch (CoreException e)
		{
			if (Activator.DEBUG)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Adds markers to a file based on the result. if errors occurred the problem markers are first cleared.
	 * 
	 * @param file
	 *            the file where the markers should be set
	 * @param result
	 *            the result indicating if parse errors occurred
	 * @throws CoreException
	 */
	private void setFileMarkers(IFile file, ParseResult result)
			throws CoreException
	{
		if (file != null)
		{
			FileUtility.deleteMarker(file,IMarker.PROBLEM,ICoreConstants.PLUGIN_ID);
			if (result.hasParseErrors())
			{
				file.deleteMarkers(IMarker.PROBLEM,
						true,
						IResource.DEPTH_INFINITE);
				int previousErrorNumber = -1;
				for (VDMError error : result.getErrors())
				{
					if (previousErrorNumber == error.number)// this check is
						// done to avoid
						// error fall
						// through
						continue;
					else
						previousErrorNumber = error.number;
					FileUtility.addMarker(file,
							error.message,
							error.location,
							IMarker.SEVERITY_ERROR,
							ICoreConstants.PLUGIN_ID);
				}
			}

			IVdmProject vdmProject = null;
			if (VdmProject.isVdmProject(project))
			{
				try
				{
					vdmProject = new VdmProject(project);
				} catch (Exception e)
				{

				}
			}

			if (result.getWarnings().size() > 0 && vdmProject != null
					&& vdmProject.hasSuppressWarnings())
			{
				for (VDMWarning warning : result.getWarnings())
				{
					FileUtility.addMarker(file,
							warning.message,
							warning.location,
							IMarker.SEVERITY_WARNING,
							ICoreConstants.PLUGIN_ID);
				}
			}
		}

	}

	/**
	 * Delegate method for parsing, the methods addWarning and addError should be used to report
	 * 
	 * @param file
	 *            the file to be parsed
	 * @param content
	 *            the content of the file
	 * @param charset
	 *            the charset of the content
	 * @return the result of the parse including error report and the AST
	 */
	protected abstract ParseResult startParse(IFile file, String content,
			String charset);

	@SuppressWarnings("unchecked")
	protected void setParseAst(String filePath, List ast,
			boolean parseErrorsOccured)
	{
		IAstManager astManager = AstManager.instance();
		astManager.updateAst(project, project.getVdmNature(), ast);
		IVdmElement rootNode = astManager.getRootNode(project, natureId);
		if (rootNode != null)
		{

			rootNode.setParseCorrect(filePath, !parseErrorsOccured);

		}
	}

	protected void addWarning(IFile file, String message, int lineNumber)
	{
		FileUtility.addMarker(file,
				message,
				lineNumber,
				IMarker.SEVERITY_WARNING);
	}

	protected void addError(IFile file, String message, int lineNumber)
	{
		FileUtility.addMarker(file, message, lineNumber, IMarker.SEVERITY_ERROR);
	}

	protected void processInternalError(Throwable e)
	{
		System.out.println(e.toString());
	};

	@SuppressWarnings("unchecked")
	public class ParseResult
	{
		private List ast = null;
		private List<VDMError> errors = new ArrayList<VDMError>();
		private List<VDMWarning> warnings = new ArrayList<VDMWarning>();
		private Throwable fatalError;

		public ParseResult() {

		}

		

		public boolean hasParseErrors()
		{
			return errors.size()!=0 || fatalError!=null;
		}

		public void setAst(List ast)
		{
			this.ast = ast;
		}

		public List getAst()
		{
			return ast;
		}

		public void setWarnings(List<VDMWarning> warnings)
		{
			this.warnings.addAll(warnings);
		}

		public List<VDMWarning> getWarnings()
		{
			return warnings;
		}

		public void setErrors(List<VDMError> errors)
		{
			this.errors.addAll(errors);
		}

		public List<VDMError> getErrors()
		{
			return errors;
		}

		public void setFatalError(Throwable fatalError)
		{
			if (Activator.DEBUG)
			{
				fatalError.printStackTrace();
			}
			this.fatalError = fatalError;
		}

		public Throwable getFatalError()
		{
			return fatalError;
		};

	}
}
